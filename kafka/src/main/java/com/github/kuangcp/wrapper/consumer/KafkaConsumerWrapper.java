package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.config.KafkaConfigManager;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:35
 */
@Slf4j
public class KafkaConsumerWrapper {

  private static Properties properties = KafkaConfigManager.getConsumerConfig();

  static void consumer(Duration duration, Collection<String> topics) {
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(topics);
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        log.info("offset = {}, value = {}", record.offset(), record.value());
      }
    }
  }
}
