package my.kafka.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Primary;
import my.kafka.service.model.Message;
import org.apache.avro.Schema;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Primary
public class MesssageConverter extends BaseConverter<Message> {

  @Inject
  public MesssageConverter(ObjectMapper objectMapper) {
    super(objectMapper, Message.class);
  }

  @Override
  Schema getSchema() {
    return schemaFromResource("./avro-schemas/message.avsc");
  }
}
