package my.kafka.service.converter;

import my.kafka.service.model.Message;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Singleton
public class MesssageConverter extends Converter<Message> {

  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

  Schema buildSchema() {
    Schema schema = SchemaBuilder.record("Message")
        .namespace("my.kafka.service.model.avro")
        .fields()
        .requiredString("id")
        .requiredString("traceId")
        .requiredString("sendDate")
        .requiredString("message")
        .endRecord();
    return schema;
  }

  @Override
  Schema getSchema() {
    //return buildSchema();
    return schemaFromResource("./avro-schemas/message.avsc");
  }

  @Override
  GenericRecord fillRecord(GenericRecord record, Message message) {
    record.put("id", message.getId().toString());
    record.put("traceId", message.getTraceId().toString());
    record.put("sendDate", sdf.format(message.getSendDate()));
    record.put("message", message.getMessage());
    return record;
  }

  @Override
  public Message fromRecord(GenericRecord record) {
    Message message = new Message();
    message.setId(UUID.fromString(record.get("id").toString()));
    message.setTraceId(UUID.fromString(record.get("traceId").toString()));
    try {
      message.setSendDate(sdf.parse(record.get("sendDate").toString()));
    } catch (ParseException e) {
      LOG.error("Error reading sendDate", e);
    }
    message.setMessage((String) record.get("message").toString());
    return message;
  }
}
