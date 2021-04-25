package com.mrg.aws.service;

import com.mrg.aws.model.Image;
import com.mrg.aws.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.*;

@Service
public class AWSServiceImpl implements AWSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSServiceImpl.class);

    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.dynamodb.table}")
    private String tableName;

    private S3Client s3Client;
    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    public AWSServiceImpl(S3Client s3Client, DynamoDbClient dynamoDbClient) {
        this.s3Client = s3Client;
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }


    @Override
    public List<Image> findAll() {
        List<Image> imageList = new ArrayList<>();
        try {
            // Create a DynamoDbTable object
            DynamoDbTable<Image> imageTable = enhancedClient.table(tableName,TableSchema.fromBean(Image.class));
            imageTable.scan().items().forEach(imageList::add);
            return imageList;

        } catch (DynamoDbException e) {
            LOGGER.error("Error= {} while searching DynamoDB.", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Image> findBySearchTerm(String searchTerm) {
        List<Image> imageList = new ArrayList<>();
        try {
            DynamoDbTable<Image> imageTable = enhancedClient.table(tableName,TableSchema.fromBean(Image.class));
            AttributeValue attr = AttributeValue.builder()
                    .s(searchTerm)
                    .build();
            Map<String, AttributeValue> myMap = new HashMap<>();
            myMap.put(":tag", attr);
            Expression expression = Expression.builder()
                    .expressionValues(myMap)
                    .expression("contains(Tags, :tag)")
                    .build();
            ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder()
                    .filterExpression(expression)
                    .build();
            imageTable.scan(enhancedRequest).items().forEach(imageList::add);
            return imageList;

        } catch (DynamoDbException e) {
            LOGGER.error("Error= {} while searching DynamoDB.", e.getMessage());
            throw e;
        }
    }

    @Override
    public byte[] downloadImage(UUID uniqueFileId) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileId.toString())
                    .build();
            ResponseBytes<GetObjectResponse> result = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());
            // to get the bytes
            return result.asByteArray();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to download file from s3 +[" + uniqueFileId + "]", e);
        }
    }

    @Override
    public void uploadImage(final MultipartFile multipartFile, final String description, final String tags) throws Exception {
        if (FileUtils.isFileNotEmpty(multipartFile)) {
            LOGGER.info("File upload in progress.");
            // Primary key for S3 & DynamoDB records
            final String uniqueFileId = UUID.randomUUID().toString();
            // DynamoDB operation
            addFileToDynamoDBTable(uniqueFileId, multipartFile, description, tags);
            // S3 operation
            uploadFileToS3Bucket(uniqueFileId, multipartFile);
            LOGGER.info("File upload is completed successfully.");
        }
    }


    private void addFileToDynamoDBTable(String uniqueId, MultipartFile multipartFile, String description, String tags) {
        try {
            LOGGER.info("Adding DynamoDB record with name= " + uniqueId);
            DynamoDbTable<Image> imageTable = enhancedClient.table(tableName,
                    TableSchema.fromBean(Image.class));
            Image image = new Image();
            image.setImageId(uniqueId);
            image.setFileName(multipartFile.getOriginalFilename());
            image.setFileDesc(description);
            image.setFileType(multipartFile.getContentType());
            image.setFileSize(String.valueOf(multipartFile.getSize()));
            image.setTags(tags);
            // Put the customer data into a DynamoDB table
            imageTable.putItem(image);
            LOGGER.info("File upload DynamoDB is completed.");
        } catch (DynamoDbException ex) {
            LOGGER.error("Error= {} while adding new file to DynamoDB.", ex.getMessage());
            throw ex;
        }
    }

    private void uploadFileToS3Bucket(final String uniqueFileId, final MultipartFile multipartFile) throws Exception {

        LOGGER.info("Uploading file with name= " + uniqueFileId);
        final File file = FileUtils.convertMultiPartFileToFile(multipartFile);
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileId)
                    .build();
            s3Client.putObject(objectRequest, RequestBody.fromFile(file));
            LOGGER.info("File upload S3 is completed.");

        } catch (Exception ex) {
            LOGGER.error("Error= {} while uploading file to S3.", ex.getMessage());
            // Rollback operation for logical data consistency
            rollBackFromDynamoDBTable(uniqueFileId);
            throw ex;
        } finally {
            FileUtils.deleteIfExist(file);
        }
    }


    /*
     * TR : S3 operasyonu başarısız olursa mantıksal veri bütünlüğünü korumak
     *      adına ilgili DynamoDB kaydı silinmeli/geri alınmalı.
     * -------------------------------------------------------------------------------
     * EN : If S3 operation fails ensures the remaining DynamoDB record is rolled back
     *      for logical data consistency.
     * */
    private void rollBackFromDynamoDBTable(String uniqueFileId) {
        try {

            LOGGER.info("Rollback started for DynamoDB record.");
            HashMap<String, AttributeValue> keyToGet = new HashMap<String, AttributeValue>();
            keyToGet.put("id", AttributeValue.builder()
                    .s(uniqueFileId)
                    .build());
            DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(keyToGet)
                    .build();
            dynamoDbClient.deleteItem(deleteReq);
            LOGGER.info("Rollback is succeeded.");
        } catch (Exception ex) {
            LOGGER.error("Rollback operation failed. Error = {}", ex.getMessage());
        }
    }
}
