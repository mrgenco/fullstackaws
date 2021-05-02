## This is a demo of the Image Gallery application I created using React, SpringBoot. 
## AWS Java SDK 2.0 is used in the backend for DynamoDB & S3 interactions.

![Preview of Demo](https://raw.githubusercontent.com/mrgenco/fullstackaws/main/api/src/main/resources/ImageGallery.JPG)

- The images are stored in an AWS S3 bucket (Create a free-tier AWS account if you don’t already have one)
- The information of the images (description, file type, size, tags, create date besides a primary key) is stored in AWS DynamoDB.
- If DB or S3 operation fails, we are ensuring the remaining record is removed/rolled back.


```java

@PostMapping(value = "/upload")
public ResponseEntity<String> uploadImage(@RequestPart(value = "image") final MultipartFile multipartFile,
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

The Image class here represents our domain object as well as DynamoDB table on AWS.
The AWS SDK for Java provides a DynamoDBMapper class, allowing you to map your client-side classes to Amazon DynamoDB tables.

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

    @Override
    public String toString() {
        return "Image{" + "imageId='" + imageId + '\'' + ", fileName='" + fileName + '\'' + ", fileDesc='" + fileDesc
                + '\'' + ", fileSize='" + fileSize + '\'' + ", fileType='" + fileType + '\'' + ", tags='" + tags + '\''
                + '}';
    }
}
```
