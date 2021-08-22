/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.storage;

import com.klindziuk.aws.local.sns.sqs.model.domain.ItemInfo;
import java.util.UUID;

public final class ItemInfoMockStorage {

  private ItemInfoMockStorage() {}

  public static ItemInfo getMockItemInfo() {
    return new ItemInfo()
        .setId(UUID.randomUUID().toString())
        .setItemName("item-info-mock-name")
        .setItemUrl("http://127.0.0.1:4566/view/item-info-mock-name");
  }
}
