/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.model.event;

import com.klindziuk.aws.local.sns.sqs.model.domain.ItemInfo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PublishEvent {

  private String publishEventId;
  private PublishEventType publishEventType;
  private LocalDateTime publishedAt;
  private ItemInfo itemInfo;
}
