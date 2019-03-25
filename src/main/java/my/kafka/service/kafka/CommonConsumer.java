package my.kafka.service.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import my.kafka.service.converter.BaseConverter;
import my.kafka.service.converter.Converter;
import my.kafka.service.model.Message;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Класс читает сообщения из кафки
 */
@KafkaListener(value = "commonConsumer", offsetReset = OffsetReset.EARLIEST)
public class CommonConsumer {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Inject
  Converter<Message> converter;

  @Topic(patterns = "${kafka.consumers.topic-in}")
  public void receive(@Topic String topic, @KafkaKey String key, GenericRecord record) {
    Message message = converter.fromRecord(record);
    LOG.debug("\nMessage received. topic '{}', key: '{}', msg:'{}'", topic, key, message);
  }
}

