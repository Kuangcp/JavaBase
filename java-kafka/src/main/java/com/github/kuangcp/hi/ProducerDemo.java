package com.github.kuangcp.hi;

import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;
import static com.github.kuangcp.hi.Constants.TOPIC;

import com.github.kuangcp.io.ResourceTool;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author kuangcp
 *
 * Date: 2019-05-20 00:04
 */
@Slf4j
public class ProducerDemo {

  public static void main(String[] args) {
    Properties properties = getConf();

    Producer<String, String> producer = null;
    try {
      producer = new KafkaProducer<>(properties);
      for (int i = 0; i < 100; i++) {
        String msg = "Message " + i;
        producer.send(new ProducerRecord<>(TOPIC, msg));
        log.info("Sent: {}", msg);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        ResourceTool.close(producer);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private static Properties getConf() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", KAFKA_SERVER);
    properties.put("acks", "all");
    properties.put("retries", 0);
    properties.put("batch.size", 16384);
    properties.put("linger.ms", 1);
    properties.put("buffer.memory", 33554432);
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    return properties;
  }
}
