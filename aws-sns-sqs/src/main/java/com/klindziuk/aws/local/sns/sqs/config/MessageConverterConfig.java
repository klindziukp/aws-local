/*
 * Copyright (c) 2021. Dandelion development
 */

package com.klindziuk.aws.local.sns.sqs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class MessageConverterConfig {

  @Bean(name = "objectMapper")
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean(name = "messageConverter")
  protected MessageConverter messageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter mappingJackson2MessageConverter =
        new MappingJackson2MessageConverter();
    mappingJackson2MessageConverter.setObjectMapper(objectMapper);
    mappingJackson2MessageConverter.setSerializedPayloadClass(String.class);
    mappingJackson2MessageConverter.setStrictContentTypeMatch(false);
    return mappingJackson2MessageConverter;
  }
}
