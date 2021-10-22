/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.service.impl;

import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;
import com.klindziuk.aws.local.sns.sqs.service.MessageConsumerService;
import com.klindziuk.aws.local.sns.sqs.storage.PublishEventStorage;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumerServiceImpl implements MessageConsumerService {

  @Override
  @SqsListener(
      value = "${config.aws.sqs.queue-name}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void consumeMessage(
      @NotificationMessage PublishEvent publishEvent, String publishEventId) {
    if (Objects.nonNull(publishEvent)) {
      log.info("Publish event consumed: {}", publishEvent);
      PublishEventStorage.addPublishEventToStorage(publishEventId, publishEvent);
    }
  }
}
