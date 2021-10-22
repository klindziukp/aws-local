/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import io.awspring.cloud.core.env.ResourceIdResolver;
import io.awspring.cloud.core.env.StackResourceRegistryDetectingResourceIdResolver;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MessageConverter;

@Data
@Configuration
public class AwsSnsConfig {

  @Value("${config.aws.region}")
  private String region;

  @Value("${config.aws.sns.url}")
  private String snsEndpointUrl;

  @Value("${config.aws.sns.access-key}")
  private String accessKey;

  @Value("${config.aws.sns.secret-key}")
  private String secretKey;

  @Value("${config.aws.sns.topic-name}")
  private String topicName;

  @Primary
  @Bean(name = "amazonSNS")
  public AmazonSNS amazonSNS() {
    return AmazonSNSClientBuilder.standard()
        .withCredentials(getCredentialsProvider())
        .withEndpointConfiguration(getEndpointConfiguration(snsEndpointUrl))
        .build();
  }

  @Bean(name = "notificationMessagingTemplate")
  public NotificationMessagingTemplate notificationMessagingTemplate(
      AmazonSNS amazonSNS, MessageConverter messageConverter) {
    ResourceIdResolver resourceIdResolver = new StackResourceRegistryDetectingResourceIdResolver();
    return new NotificationMessagingTemplate(amazonSNS, resourceIdResolver, messageConverter);
  }

  private EndpointConfiguration getEndpointConfiguration(String url) {
    return new EndpointConfiguration(url, region);
  }

  private AWSStaticCredentialsProvider getCredentialsProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
  }
}
