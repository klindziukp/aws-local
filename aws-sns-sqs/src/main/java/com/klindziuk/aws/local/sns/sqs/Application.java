package com.klindziuk.aws.local.sns.sqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
      io.awspring.cloud.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
      io.awspring.cloud.autoconfigure.context.ContextStackAutoConfiguration.class,
      io.awspring.cloud.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
    })
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
