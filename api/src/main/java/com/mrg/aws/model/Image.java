package com.mrg.aws.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

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

    @Override
    public String toString() {
        return "Image{" + "imageId='" + imageId + '\'' + ", fileName='" + fileName + '\'' + ", fileDesc='" + fileDesc
                + '\'' + ", fileSize='" + fileSize + '\'' + ", fileType='" + fileType + '\'' + ", tags='" + tags + '\''
                + '}';
    }
}
