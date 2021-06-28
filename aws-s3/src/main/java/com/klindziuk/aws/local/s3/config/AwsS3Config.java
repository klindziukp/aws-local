/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AwsS3Config {

  @Value("${config.aws.region}")
  private String region;

  @Value("${config.aws.s3.url}")
  private String s3EndpointUrl;

  @Value("${config.aws.s3.bucket-name}")
  private String bucketName;

  @Value("${config.aws.s3.access-key}")
  private String accessKey;

  @Value("${config.aws.s3.secret-key}")
  private String secretKey;

  @Bean(name = "amazonS3")
  public AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(getCredentialsProvider())
        .withEndpointConfiguration(getEndpointConfiguration(s3EndpointUrl))
        .build();
  }

  private EndpointConfiguration getEndpointConfiguration(String url) {
    return new EndpointConfiguration(url, region);
  }

  private AWSStaticCredentialsProvider getCredentialsProvider() {
    return new AWSStaticCredentialsProvider(getBasicAWSCredentials());
  }

  private BasicAWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials(accessKey, secretKey);
  }
}
