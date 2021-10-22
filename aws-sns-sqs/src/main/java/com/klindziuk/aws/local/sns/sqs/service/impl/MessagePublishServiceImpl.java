/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.service.impl;

import com.klindziuk.aws.local.sns.sqs.config.AwsSnsConfig;
import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;
import com.klindziuk.aws.local.sns.sqs.service.MessagePublishService;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePublishServiceImpl implements MessagePublishService {

  private final NotificationMessagingTemplate notificationMessagingTemplate;
  private final AwsSnsConfig awsSnsConfig;

  @Autowired
  public MessagePublishServiceImpl(
      NotificationMessagingTemplate notificationMessagingTemplate, AwsSnsConfig awsSnsConfig) {
    this.notificationMessagingTemplate = notificationMessagingTemplate;
    this.awsSnsConfig = awsSnsConfig;
  }

  public void publishMessage(PublishEvent publishEvent) {
    notificationMessagingTemplate.convertAndSend(awsSnsConfig.getTopicName(), publishEvent);
  }
}
