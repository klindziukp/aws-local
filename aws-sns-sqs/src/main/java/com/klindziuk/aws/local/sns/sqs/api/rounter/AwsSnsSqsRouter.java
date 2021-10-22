/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.api.rounter;

import com.klindziuk.aws.local.sns.sqs.api.handler.AwsSnsSqsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AwsSnsSqsRouter {

  @Bean
  public RouterFunction<ServerResponse> awsDynamoDBRouterFunction(
      AwsSnsSqsHandler awsSnsSqsHandler) {
    RequestPredicate savePublishMessage =
        RequestPredicates.POST("/sns-sqs/save")
            .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

    RequestPredicate viewPublishMessage =
        RequestPredicates.GET("/sns-sqs/view")
            .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

    RequestPredicate deletePublishMessage =
        RequestPredicates.DELETE("/sns-sqs/delete")
            .and(RequestPredicates.accept(MediaType.APPLICATION_JSON));

    return RouterFunctions.route(savePublishMessage, awsSnsSqsHandler::publishSaveItemInfoEvent)
        .andRoute(viewPublishMessage, awsSnsSqsHandler::publishViewItemInfoEvent)
        .andRoute(deletePublishMessage, awsSnsSqsHandler::publishDeleteItemInfoEvent);
  }
}
