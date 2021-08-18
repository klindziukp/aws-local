/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.dynamodb.api.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.klindziuk.aws.local.dynamodb.constant.PathVariableParam;
import com.klindziuk.aws.local.dynamodb.domain.ItemInfo;
import com.klindziuk.aws.local.dynamodb.exception.UnableToFindItemInfoException;
import com.klindziuk.aws.local.dynamodb.service.ItemInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Configuration
@Slf4j
public class AwsDynamoDbHandler {

  private final ItemInfoService itemInfoService;

  @Autowired
  public AwsDynamoDbHandler(ItemInfoService itemInfoService) {
    this.itemInfoService = itemInfoService;
  }

  @NonNull
  public Mono<ServerResponse> getItemByName(ServerRequest request) {
    final String itemName = request.pathVariable(PathVariableParam.ITEM_NAME);
    Mono<ItemInfo> fileInfo = itemInfoService.getItemInfoByName(itemName);
    return ok().contentType(APPLICATION_JSON)
        .body(fileInfo, ItemInfo.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  @NonNull
  public Mono<ServerResponse> viewAllItemInfo(ServerRequest request) {
    return ok().contentType(APPLICATION_JSON)
        .body(itemInfoService.findAllItemInfo(), ItemInfo.class);
  }

  @NonNull
  public Mono<ServerResponse> saveItemInfo(ServerRequest request) {
    return request
        .bodyToMono(ItemInfo.class)
        .doOnNext(itemInfoService::saveItemInfo)
        .flatMap(itemInfo -> ok().contentType(APPLICATION_JSON).body(Mono.just(itemInfo), ItemInfo.class));
  }

  @NonNull
  public Mono<ServerResponse> deleteItemInfo(ServerRequest request) {
    final String itemName = request.pathVariable(PathVariableParam.ITEM_NAME);
    return itemInfoService.getItemInfoByName(itemName).flatMap(itemInfo -> {
      itemInfoService.deleteItemInfo(itemInfo.getItemName());
      return ok().contentType(APPLICATION_JSON).body(Mono.just(itemInfo), ItemInfo.class);
    }).switchIfEmpty(ServerResponse.notFound().build());
  }
}
