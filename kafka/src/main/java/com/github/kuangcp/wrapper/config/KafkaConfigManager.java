package com.github.kuangcp.wrapper.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:37
 */
@Slf4j
public class KafkaConfigManager {

  private static Properties load;

  static {
    InputStream is = KafkaConfigManager.class.getResourceAsStream("/kafka.properties");
    try {
      load = new Properties();
      load.load(is);
    } catch (IOException e) {
      log.error("", e);
    }
  }

  public static Optional<Properties> getConsumerConfig() {
    Optional<String> serverOpt = Optional.ofNullable(load)
        .map(v -> v.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    if (!serverOpt.isPresent()) {
      return Optional.empty();
    }

    String groupId;
    Optional<String> groupOpt = Optional.ofNullable(load)
        .map(v -> v.getProperty(ConsumerConfig.GROUP_ID_CONFIG));
    groupId = groupOpt.orElseGet(() -> "Group-" + UUID.randomUUID().toString());

    Properties config = new Properties();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverOpt.get());
    config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    return Optional.of(config);
  }

  public static Optional<Properties> getProducerConfig() {
    Optional<String> serverOpt = Optional.ofNullable(load)
        .map(v -> v.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    if (!serverOpt.isPresent()) {
      return Optional.empty();
    }

    Properties config = new Properties();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverOpt.get());
    config.put(ProducerConfig.ACKS_CONFIG, "all");
    config.put(ProducerConfig.RETRIES_CONFIG, 0);
    config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
    config.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return Optional.of(config);
  }
}
