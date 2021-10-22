/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.service;

import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;

public interface MessageConsumerService {

  void consumeMessage(@NotificationMessage PublishEvent publishEvent, String publishEventId);
}
