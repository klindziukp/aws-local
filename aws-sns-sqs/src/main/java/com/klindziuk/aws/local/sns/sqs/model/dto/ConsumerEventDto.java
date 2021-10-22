/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.model.dto;

import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConsumerEventDto {

  private PublishEvent publishEvent;
  private LocalDateTime consumedAt;
  private ConsumeEventResult consumeEventResult;
}
