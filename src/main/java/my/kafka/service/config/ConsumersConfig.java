package my.kafka.service.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;

@ConfigurationProperties("kafka.consumers")
public class ConsumersConfig {

  @Property(name = "topic-in")
  private String topicIn;

  public String getTopicIn() {
    return topicIn;
  }
}
