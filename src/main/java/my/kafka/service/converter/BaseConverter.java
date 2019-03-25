package my.kafka.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public abstract class BaseConverter<T> implements Converter<T> {
  final Logger LOG = LoggerFactory.getLogger(getClass());

  private ObjectMapper objectMapper;

  private final Class<T> type;

  public BaseConverter(ObjectMapper objectMapper, Class<T> type) {
    this.objectMapper = objectMapper;
    this.type = type;
  }

  abstract Schema getSchema();

  private GenericRecord recordFromJson(Schema schema, String json) {
    GenericRecord genericRecord = null;
    try {
      JsonDecoder jsonDecoder = DecoderFactory.get().jsonDecoder(schema, json);
      DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
      genericRecord = datumReader.read(null, jsonDecoder);
    } catch (IOException e) {
      LOG.error("Error writing record to json", e);
    }

    return genericRecord;
  }

  Schema schemaFromResource(String schemaPath) {
    String schemaString = null;
    URL url = Resources.getResource(schemaPath);
    try {
      schemaString = Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      LOG.error("Error reading avro schema", e);
    }
    return schemaString != null ? new Schema.Parser().parse(schemaString) : null;
  }

  @Override
  public GenericRecord toRecord(T t) {

    GenericRecord record = null;
    Schema schema = getSchema();
    try {
      String json = objectMapper.writeValueAsString(t);
      record = recordFromJson(schema, json);
    } catch (JsonProcessingException e) {
      LOG.error("Error writing record", e);
    }

    return record;
  }

  @Override
  public T fromRecord(GenericRecord record) {
    String json = record.toString();
    T obj = null;
    try {
      obj = objectMapper.readValue(json, type);
    } catch (IOException e) {
      LOG.error("Error reading record", e);
    }
    return obj;
  }
}
