package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.config.KafkaConfigManager;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.CollectionUtils;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:35
 */
@Slf4j
public class KafkaConsumerWrapper {

  private static ExecutorService pool = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static Properties properties = KafkaConfigManager.getConsumerConfig()
      .orElseThrow(() -> new RuntimeException(""));

  static <T> void consumer(Duration duration, List<SimpleTopicMessageExecutor> executors) {
    if (Objects.isNull(duration) || Objects.isNull(executors) || executors.isEmpty()) {
      log.warn("consumer param invalid");
      return;
    }

    Map<String, SimpleTopicMessageExecutor> executorMap = executors.stream()
        .collect(Collectors.toMap(SimpleTopicMessageExecutor::getTopic,
            Function.identity(), (front, current) -> current));

    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(executorMap.keySet());
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        SimpleTopicMessageExecutor executor = executorMap.get(record.topic());

        if (Objects.nonNull(executor)) {
          pool.submit(() -> executor.execute(record.value()));
        }
      }
    }
  }

  static <T> void consumers(Duration duration, List<SingleTopicMessageExecutor<T>> executors) {
    if (Objects.isNull(duration) || Objects.isNull(executors) || executors.isEmpty()) {
      log.warn("consumer param invalid");
      return;
    }

    Map<String, SingleTopicMessageExecutor<T>> executorMap = executors.stream()
        .collect(Collectors.toMap(SingleTopicMessageExecutor::getTopic,
            Function.identity(), (front, current) -> current));

    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(executorMap.keySet());
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        SingleTopicMessageExecutor<T> executor = executorMap.get(record.topic());

        if (Objects.nonNull(executor)) {
          pool.submit(() -> executor.execute(record.value()));
        }
      }
    }
  }
}
