/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.dynamodb.repository;

import com.klindziuk.aws.local.dynamodb.domain.ItemInfo;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ItemInfoRepository extends CrudRepository<ItemInfo, String> {

  ItemInfo findByItemName(String itemName);

  void deleteByItemName(String itemName);
}
