package my.kafka.service.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import my.kafka.service.model.Message;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.concurrent.*;

/**
 * Класс отправляет сообщения в кафку
 */
@Singleton
public class CommonProducer {
  private final Logger LOG = LoggerFactory.getLogger(getClass());
  private final KafkaProducer<String, GenericRecord> kafkaProducer;

  public CommonProducer(@KafkaClient(value = "commonProducer", acks = KafkaClient.Acknowledge.ALL)
                            KafkaProducer<String, GenericRecord> kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  public void sendAsync(String topic, String key, GenericRecord msg) {
    CompletableFuture.supplyAsync(() -> {
      try {
        Future<RecordMetadata> future = kafkaProducer.send(new ProducerRecord<>(topic, null, key, msg));
        return future.get(2, TimeUnit.SECONDS);
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        throw new RuntimeException(e);
      }
    }).handle((recordMetadata, throwable) -> {
      if (throwable != null) {
        LOG.error("Error while sending msg {} to topic: {}", msg, topic, throwable);
      } else {
        LOG.debug("\nMessage sent. topic '{}', key: '{}', msg:'{}'", topic, key, msg);
      }
      return recordMetadata;
    });
  }
}
