package my.kafka.service.converter;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public abstract class Converter<T> {
  final Logger LOG = LoggerFactory.getLogger(getClass());

  public GenericRecord toRecord(T t) {
    GenericRecord record = new GenericData.Record(getSchema());
    return fillRecord(record, t);
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

  abstract GenericRecord fillRecord(GenericRecord record, T t);

  abstract Schema getSchema();

  abstract public T fromRecord(GenericRecord record);
}
