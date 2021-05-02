###### This is an example Full-Stack Image Gallery application built with React & SpringBoot
###### AWS Java SDK 2.0 is used for DynamoDB & S3 interactions.

Basic Requirements: NodeJS, JRE 1.8+, AWS Account

[Live Demo on Youtube](https://www.youtube.com/watch?v=Sm_7TgMi9PY)

![Preview of Demo](https://raw.githubusercontent.com/mrgenco/fullstackaws/main/api/src/main/resources/ImageGallery.JPG)


- The images are stored in an AWS S3 bucket (Create a free-tier AWS account if you don’t already have one)
- The information of the images (description, file type, size, tags, create date besides a primary key) is stored in AWS DynamoDB.
- If DB or S3 operation fails, we are ensuring the remaining record is removed/rolled back.


### Upload, Download and Search Endpoints

```java

@PostMapping(value = "/upload")
public ResponseEntity<String> uploadImage(
        @RequestPart(value = "image") final MultipartFile multipartFile,
        @RequestPart(value = "description") final String description,
        @RequestPart(value = "tags") final String tags) {
    try {
        service.uploadImage(multipartFile, description, tags);
        final String response = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception ex) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/download/{imageId}")
public byte[] downloadImage(@PathVariable("imageId") UUID imageId) {
    return service.downloadImage(imageId);
}

@GetMapping(value = "/search/{searchTerm}")
public List<Image> searchImage(@PathVariable("searchTerm") String searchTerm) {
    return service.findBySearchTerm(searchTerm);
}
```

Below example in AWSServiceImpl.java class shows DynamoDbEnhancedClient usage which is a new module of the AWS SDK for Java 2.0
Read [this blog post](https://aws.amazon.com/blogs/developer/introducing-enhanced-dynamodb-client-in-the-aws-sdk-for-java-v2/) for details


```java
@Override
public List<Image> findBySearchTerm(String searchTerm) {
        try {            
            List<Image> imageList = new ArrayList<>();
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
            DynamoDbTable<Image> imageTable = enhancedClient.table(tableName, TableSchema.fromBean(Image.class));
            AttributeValue attr = AttributeValue.builder().s(searchTerm).build();
            Map<String, AttributeValue> myMap = new HashMap<>();
            myMap.put(":tag", attr);
            Expression expression = Expression.builder().expressionValues(myMap).expression("contains(Tags, :tag)").build();
            ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder().filterExpression(expression).build();
            imageTable.scan(enhancedRequest).items().forEach(imageList::add);
            return imageList;
        } catch (DynamoDbException e) {
            LOGGER.error("Error= {} while searching DynamoDB.", e.getMessage());
            throw e;
        }
}

```

The AWS SDK for Java provides a DynamoDBMapper class, allowing you to map your client-side classes to Amazon DynamoDB tables.
The Image class below represents our domain object as well as DynamoDB table on AWS.

```java
@DynamoDbBean
public class Image {

    private String imageId;
    private String fileName;
    private String fileDesc;
    private String fileSize;
    private String fileType;
    private String tags;
    private String createDate;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "ImageId")
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @DynamoDbAttribute(value = "FileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @DynamoDbAttribute(value = "FileDesc")
    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    @DynamoDbAttribute(value = "FileSize")
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @DynamoDbAttribute(value = "FileType")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @DynamoDbAttribute(value = "Tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @DynamoDbAttribute(value = "CreateDate")
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
```
