package my.kafka.service.converter;

import io.micronaut.context.annotation.Primary;
import my.kafka.service.model.Message;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Singleton
//@Primary
public class MessageSimpleConverter implements Converter<Message> {

  final Logger LOG = LoggerFactory.getLogger(getClass());

  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

  public MessageSimpleConverter() {
  }

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
  public GenericRecord toRecord(Message message) {

    Schema schema = buildSchema();

    GenericRecord record = new GenericData.Record(schema);
    //write DTO to record
    record.put("id", message.getId().toString());
    record.put("traceId", message.getTraceId().toString());
    record.put("sendDate", sdf.format(message.getSendDate()));
    record.put("message", message.getMessage());

    return record;
  }

  @Override
  public Message fromRecord(GenericRecord record) {
    Message message = new Message();
    //read DTO from record
    message.setId(UUID.fromString(record.get("id").toString()));
    message.setTraceId(UUID.fromString(record.get("traceId").toString()));
    try {
      message.setSendDate(sdf.parse(record.get("sendDate").toString()));
    } catch (ParseException e) {
      LOG.error("Error reading field 'sendDate'", e);
    }
    message.setMessage(record.get("message").toString());

    return message;
  }
}
