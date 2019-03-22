package my.kafka.service.model;

import org.apache.avro.generic.IndexedRecord;

import java.util.Date;
import java.util.UUID;

/**
 * Класс данных для Kafka
 */
public class Message {
  //идентификатор сообщения, совпадает с идентификатором из очереди на отправку
  private UUID id = UUID.randomUUID();
  // трассировочный id
  private UUID traceId = UUID.randomUUID();
  // дата отправки
  private Date sendDate = new Date();
  // текст ошибки, если такова была
  private String message;

  public Message() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getTraceId() {
    return traceId;
  }

  public void setTraceId(UUID traceId) {
    this.traceId = traceId;
  }

  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", traceId=" + traceId +
        ", sendDate=" + sendDate +
        ", message='" + message + '\'' +
        '}';
  }
}
