/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.klindziuk.aws.local.s3.config.AwsS3Config;
import com.klindziuk.aws.local.s3.domain.FileInfo;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AwsS3Service {

  private final AmazonS3 amazonS3;
  private final AwsS3Config awsS3Config;

  @Autowired
  public AwsS3Service(AmazonS3 amazonS3, AwsS3Config awsS3Config) {
    this.amazonS3 = amazonS3;
    this.awsS3Config = awsS3Config;
  }

  public FileInfo uploadObjectToS3(String fileName, byte[] fileData) {
    log.info("Uploading file '{}' to bucket: '{}' ", fileName, awsS3Config.getBucketName());
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    String fileUrl =
        awsS3Config.getS3EndpointUrl() + "/" + awsS3Config.getBucketName() + "/" + fileName;
    PutObjectResult putObjectResult =
        amazonS3.putObject(
            awsS3Config.getBucketName(), fileName, byteArrayInputStream, objectMetadata);
    return new FileInfo(fileName, fileUrl, Objects.nonNull(putObjectResult));
  }

  public S3ObjectInputStream downloadFileFromS3Bucket(final String fileName) {
    log.info("Downloading file '{}' from bucket: '{}' ", fileName, awsS3Config.getBucketName());
    final S3Object s3Object = amazonS3.getObject(awsS3Config.getBucketName(), fileName);
    return s3Object.getObjectContent();
  }

  public List<S3ObjectSummary> listObjects() {
    log.info("Retrieving object summaries for bucket '{}'", awsS3Config.getBucketName());
    ObjectListing objectListing = amazonS3.listObjects(awsS3Config.getBucketName());
    return objectListing.getObjectSummaries();
  }
}
