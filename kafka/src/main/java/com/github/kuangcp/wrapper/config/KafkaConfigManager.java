package com.github.kuangcp.wrapper.config;

import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:37
 */
public class KafkaConfigManager {

  public static Properties getConsumerConfig() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", KAFKA_SERVER);
    properties.put("group.id", "TestGroup-" + UUID.randomUUID().toString());
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.interval.ms", "1000");
    properties.put("auto.offset.reset", "earliest");
    properties.put("session.timeout.ms", "30000");
    properties.put("key.deserializer", StringDeserializer.class.getName());
    properties.put("value.deserializer", StringDeserializer.class.getName());
    return properties;
  }

  public static Properties getProducerConfig() {
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
