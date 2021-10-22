/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.api.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.klindziuk.aws.local.sns.sqs.model.domain.ItemInfo;
import com.klindziuk.aws.local.sns.sqs.model.dto.ConsumeEventResult;
import com.klindziuk.aws.local.sns.sqs.model.dto.ConsumerEventDto;
import com.klindziuk.aws.local.sns.sqs.model.event.PublishEvent;
import com.klindziuk.aws.local.sns.sqs.model.event.PublishEventType;
import com.klindziuk.aws.local.sns.sqs.service.MessageConsumerService;
import com.klindziuk.aws.local.sns.sqs.service.MessagePublishService;
import com.klindziuk.aws.local.sns.sqs.storage.ItemInfoMockStorage;
import com.klindziuk.aws.local.sns.sqs.storage.PublishEventStorage;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Configuration
@Slf4j
public class AwsSnsSqsHandler {

  private final MessagePublishService messagePublishService;
  private final MessageConsumerService messageConsumerService;

  @Autowired
  public AwsSnsSqsHandler(
      MessagePublishService messagePublishService, MessageConsumerService messageConsumerService) {
    this.messagePublishService = messagePublishService;
    this.messageConsumerService = messageConsumerService;
  }

  @NonNull
  public Mono<ServerResponse> publishSaveItemInfoEvent(ServerRequest request) {
    return publishItemInfoEvent(
        request.bodyToMono(ItemInfo.class), PublishEventType.ITEM_INFO_CREATED);
  }

  @NonNull
  public Mono<ServerResponse> publishViewItemInfoEvent(ServerRequest request) {
    return publishItemInfoEvent(
        Mono.just(ItemInfoMockStorage.getMockItemInfo()), PublishEventType.ITEM_INFO_VIEWED);
  }

  @NonNull
  public Mono<ServerResponse> publishDeleteItemInfoEvent(ServerRequest request) {
    return publishItemInfoEvent(
        Mono.just(ItemInfoMockStorage.getMockItemInfo()), PublishEventType.ITEM_INFO_DELETED);
  }

  private Mono<ServerResponse> publishItemInfoEvent(
      Mono<ItemInfo> itemInfoMono, PublishEventType publishEventType) {
    final String publishEventId = UUID.randomUUID().toString();
    return itemInfoMono
        .doOnNext(
            itemInfo -> {
              PublishEvent publishEvent = createPublishEvent(itemInfo, publishEventType);
              messagePublishService.publishMessage(publishEvent);
              messageConsumerService.consumeMessage(publishEvent, publishEventId);
            })
        .flatMap(
            itemInfo ->
                ok().contentType(APPLICATION_JSON)
                    .body(createConsumerEventDto(publishEventId), ConsumerEventDto.class));
  }

  private Mono<ConsumerEventDto> createConsumerEventDto(String publishEventId) {
    PublishEvent publishEvent =
        PublishEventStorage.getAndRemovePublishEventFromStorage(publishEventId);
    if (Objects.nonNull(publishEvent)) {
      return Mono.just(
          new ConsumerEventDto()
              .setPublishEvent(publishEvent)
              .setConsumedAt(LocalDateTime.now())
              .setConsumeEventResult(ConsumeEventResult.SUCCESS));
    }
    return Mono.just(
        new ConsumerEventDto()
            .setConsumedAt(LocalDateTime.now())
            .setConsumeEventResult(ConsumeEventResult.FAILURE));
  }

  private PublishEvent createPublishEvent(ItemInfo itemInfo, PublishEventType publishEventType) {
    return new PublishEvent()
        .setPublishEventId(UUID.randomUUID().toString())
        .setPublishedAt(LocalDateTime.now())
        .setItemInfo(itemInfo.setId(UUID.randomUUID().toString()))
        .setPublishEventType(publishEventType);
  }
}
