/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.dynamodb.service;

import com.klindziuk.aws.local.dynamodb.domain.ItemInfo;
import com.klindziuk.aws.local.dynamodb.exception.UnableToFindItemInfoException;
import com.klindziuk.aws.local.dynamodb.repository.ItemInfoRepository;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemInfoService {

  private final ItemInfoRepository itemInfoRepository;

  @Autowired
  public ItemInfoService(ItemInfoRepository itemInfoRepository) {
    this.itemInfoRepository = itemInfoRepository;
  }

  public Mono<ItemInfo> saveItemInfo(ItemInfo itemInfo) {
    log.info("Saving item info: '{}'", itemInfo);
    itemInfoRepository.save(Objects.requireNonNull(itemInfo));
    log.info("Item info '{}' saved successfully", itemInfo);
    return Mono.just(itemInfoRepository.findByItemName(itemInfo.getItemName()));
  }

  public void deleteItemInfo(String itemName) {
    log.info("Deleting item by name '{}'", itemName);
    itemInfoRepository.deleteByItemName(itemName);
  }

  public Flux<ItemInfo> findAllItemInfo() {
    log.info("Retrieving all items info");
    return Flux.fromIterable(itemInfoRepository.findAll());
  }

  public Mono<ItemInfo> getItemInfoByName(String itemName) {
    log.info("Retrieving item by name '{}'", itemName);
    ItemInfo itemInfo = itemInfoRepository.findByItemName(itemName);
    if (Objects.nonNull(itemInfo)) {
      return Mono.just(itemInfoRepository.findByItemName(itemName));
    }
    return Mono.empty();
  }
}
