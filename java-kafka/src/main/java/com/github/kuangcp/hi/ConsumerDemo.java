package com.github.kuangcp.hi;

import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;
import static com.github.kuangcp.hi.Constants.TOPIC;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author kuangcp
 *
 * Date: 2019-05-20 00:11
 */
@Slf4j
public class ConsumerDemo {

  public static void main(String[] args) {
    Properties properties = getConf();

    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Collections.singletonList(TOPIC));
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        log.info("offset = {}, value = %{}", record.offset(), record.value());
      }
    }

  }

  private static Properties getConf() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", KAFKA_SERVER);
    properties.put("group.id", "group-1");
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.interval.ms", "1000");
    properties.put("auto.offset.reset", "earliest");
    properties.put("session.timeout.ms", "30000");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties
        .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    return properties;
  }
}