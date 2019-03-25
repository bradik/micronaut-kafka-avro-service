package my.kafka.service.converter;

import org.apache.avro.generic.GenericRecord;

public interface Converter<T> {
  GenericRecord toRecord(T t);
  T fromRecord(GenericRecord record);
}
