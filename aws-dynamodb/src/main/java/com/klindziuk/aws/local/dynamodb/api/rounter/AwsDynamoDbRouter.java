/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.dynamodb.api.rounter;

import com.klindziuk.aws.local.dynamodb.api.handler.AwsDynamoDbHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AwsDynamoDbRouter {

  @Bean
  public RouterFunction<ServerResponse> awsDynamoDBRouterFunction(AwsDynamoDbHandler awsDynamoDbHandler) {
    RequestPredicate saveToDynamoDb =
        RequestPredicates.POST("/dynamodb/save")
            .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

    RequestPredicate getFromDynamoDbByName = RequestPredicates.GET("/dynamodb/view/{itemName}");

    RequestPredicate getAllFromDynamoDb =
        RequestPredicates.GET("/dynamodb/view-all")
            .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

    RequestPredicate deleteFromDynamoDb = RequestPredicates.DELETE("/dynamodb/{itemName}");

    return RouterFunctions.route(saveToDynamoDb, awsDynamoDbHandler::saveItemInfo)
        .andRoute(getFromDynamoDbByName, awsDynamoDbHandler::getItemByName)
        .andRoute(getAllFromDynamoDb, awsDynamoDbHandler::viewAllItemInfo)
        .andRoute(deleteFromDynamoDb, awsDynamoDbHandler::deleteItemInfo);
  }
}
