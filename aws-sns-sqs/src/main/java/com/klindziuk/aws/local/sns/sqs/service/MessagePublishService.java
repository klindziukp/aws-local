/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.service;

import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;

public interface MessagePublishService {

  void publishMessage(PublishEvent publishEvent);
}
