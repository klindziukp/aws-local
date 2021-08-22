/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.support.NotificationMessageArgumentResolver;
import java.util.Collections;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MessageConverter;

@Data
@Configuration
public class AwsSqsConfig {

  @Value("${config.aws.region}")
  private String region;

  @Value("${config.aws.sqs.url}")
  private String sqsEndpointUrl;

  @Value("${config.aws.sqs.access-key}")
  private String accessKey;

  @Value("${config.aws.sqs.secret-key}")
  private String secretKey;

  @Value("${config.aws.sqs.queue-name}")
  private String queueName;

  @Primary
  @Bean(name = "amazonSQSAsync")
  public AmazonSQSAsync amazonSQSAsync() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(getCredentialsProvider())
        .withEndpointConfiguration(getEndpointConfiguration(sqsEndpointUrl))
        .build();
  }

  @Bean(name = "queueMessagingTemplate")
  public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
    return new QueueMessagingTemplate(amazonSQSAsync);
  }

  @Bean(name = "queueMessageHandlerFactory")
  public QueueMessageHandlerFactory queueMessageHandlerFactory(
      MessageConverter messageConverter, AmazonSQSAsync amazonSQSAsync) {
    QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
    NotificationMessageArgumentResolver notificationMessageArgumentResolver =
        new NotificationMessageArgumentResolver(messageConverter);
    queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync);
    queueMessageHandlerFactory.setArgumentResolvers(
        Collections.singletonList(notificationMessageArgumentResolver));
    return queueMessageHandlerFactory;
  }

  private EndpointConfiguration getEndpointConfiguration(String url) {
    return new EndpointConfiguration(url, region);
  }

  private AWSStaticCredentialsProvider getCredentialsProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
  }
}
