/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PublishEventType {

  ITEM_INFO_CREATED("ITEM_INFO_CREATED"),
  ITEM_INFO_VIEWED("ITEM_INFO_VIEWED"),
  ITEM_INFO_DELETED("ITEM_INFO_DELETED");

  private final String value;
}
