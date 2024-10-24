package com._119.wepro.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;

@Configuration
public class S3ClientFactory {
  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secretKey}")
  private String secretKey;

  @Bean
  public S3Client s3Client(){
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(this.accessKey, this.secretKey);
    StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCredentials);


    return S3Client.builder()
        .credentialsProvider(credentialsProvider)
        .region(Region.AP_NORTHEAST_2)
        .build();
  }

  @Bean
  public S3Utilities s3Utilities(S3Client s3Client){
    return s3Client.utilities();
  }
}