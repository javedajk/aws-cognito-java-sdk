
package com.crud.awsresources;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Configuration
public class AwsConfig {


    @Value("${amazon.aws.endpointUrl}")
    private String endpointUrl;

    @Value("${amazon.aws.region}")
    private String amazonAwsRegion;

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Value("${amazon.aws.userPoolId}")
    private String userPoolId;

    @Value("${amazon.aws.clientId}")
    private String clientId;

    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    @Value("${amazon.aws.folderName}")
    private String folderName;

    private AmazonS3 s3client;

    private static Logger logger = LoggerFactory.getLogger(AwsConfig.class);

    public AwsConfig() {
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
        return credentials;
    }

    @Bean
    public AWSCognitoIdentityProvider awsCognitoIdentityProvider() {
        return AWSCognitoIdentityProviderClient.builder().withRegion(Regions.fromName(amazonAwsRegion)).withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
                .build();
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }
    
private void
uploadFileTos3bucket(String fileName,String base64,String contenttype) {
    byte file[] = Base64.decodeBase64(base64);
    InputStream fis = new ByteArrayInputStream(file);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(contenttype);
    metadata.setContentLength(file.length);
    s3client.putObject(new PutObjectRequest(bucketName, fileName, fis,metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
}

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        System.out.println(" convertMultiPartToFile ");
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public String uploadFile(String folder,String filename,String base64,String content,String extention) {
        String fileUrl = "";
        try {
            String fileName = generateFileName(folder,filename,extention);
           fileUrl = endpointUrl + "/" + bucketName + "/" + fileName ;
            logger.debug("fileurl: "+ fileUrl);
            uploadFileTos3bucket(fileName, base64,content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private String generateFileName(String folder,String filename, String extension) {
        logger.debug(" generateFileName ");
        LocalDateTime ldt = LocalDateTime.now();
        String date = DateTimeFormatter.ofPattern("ddMMyyyy", Locale.ENGLISH).format(ldt);
        logger.debug("Adding Date format to filename: "+ date);
        return folder+"/"+ filename+ "_"+ date +"_"+ new Date().getTime()  + extension  ;
    }


    public String getAmazonAwsRegion() {
        return amazonAwsRegion;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBucketName() {
        return bucketName;
    }

}

