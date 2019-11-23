package com.github.kuangcp.wrapper;

import com.github.kuangcp.wrapper.config.KafkaConfigManager;
import java.util.Objects;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:36
 */
public class KafkaProducerWrapper {

  private static Properties properties = KafkaConfigManager.getProducerConfig()
      .orElseThrow(() -> new RuntimeException(""));

  public static <T> void send(String topic, Message<T> message) {
    if (Objects.isNull(topic) || topic.isEmpty() || Objects.isNull(message)) {
      return;
    }

    Producer<String, Message<T>> producer = new KafkaProducer<>(properties);
    producer.send(new ProducerRecord<>(topic, message));
  }

  public static <T> void send(String topic, T content) {
    Producer<String, T> producer = new KafkaProducer<>(properties);
    producer.send(new ProducerRecord<>(topic, content));
  }

  public static void sendPlainText(String topic, String message) {
    Producer<String, String> producer = new KafkaProducer<>(properties);
    producer.send(new ProducerRecord<>(topic, message));
  }
}
