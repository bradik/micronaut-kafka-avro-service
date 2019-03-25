package my.kafka.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import my.kafka.service.config.ConsumersConfig;
import my.kafka.service.converter.BaseConverter;
import my.kafka.service.converter.Converter;
import my.kafka.service.converter.MessageSimpleConverter;
import my.kafka.service.converter.MesssageConverter;
import my.kafka.service.kafka.CommonProducer;
import my.kafka.service.model.*;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class KafkaTest {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaTest.class);

  private static final int TIMEOUT = 5;
  private static ApplicationContext ctx;

  @BeforeAll
  static void startServer() {
    ctx = Micronaut.run();
  }

  private void sleep() {
    try {
      TimeUnit.SECONDS.sleep(TIMEOUT);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void commonProcessTest() {

    CommonProducer producer = ctx.getBean(CommonProducer.class);
    ConsumersConfig config = ctx.getBean(ConsumersConfig.class);
    Converter<Message> converter = ctx.getBean(Converter.class);


    String topic = config.getTopicIn();
    String key = UUID.randomUUID().toString();

    Message message = new Message();
    message.setId(UUID.randomUUID());
    message.setTraceId(UUID.randomUUID());
    message.setSendDate(new Date());
    message.setMessage("success");

    GenericRecord record = converter.toRecord(message);
    producer.sendAsync(topic, key, record);

    sleep();

  }

}
