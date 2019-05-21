package com.github.kuangcp.hi;

import static com.github.kuangcp.hi.Constants.HI_TOPIC;
import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.hi.dto.StartCommand;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author kuangcp
 *
 * Date: 2019-05-20 00:11
 */
@Slf4j
public class ConsumerDemo {

  private static Properties properties = getConf();
  private static ObjectMapper mapper = new ObjectMapper();

  static void receiveHi() {
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Collections.singletonList(HI_TOPIC));
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        log.info("offset = {}, value = %{}", record.offset(), record.value());
      }
    }
  }

  static void receiveStart() throws IOException {
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Collections.singletonList("OFC_PRODUCT_STATISTIC_JOB_DISPATCHING"));
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        log.info("offset = {}, value = {}", record.offset(), record.value());
        StartCommand command = mapper.readValue(record.value(), StartCommand.class);
        System.out.println(command);
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
    properties.put("key.deserializer", StringDeserializer.class.getName());
    properties.put("value.deserializer", StringDeserializer.class.getName());
    return properties;
  }
}