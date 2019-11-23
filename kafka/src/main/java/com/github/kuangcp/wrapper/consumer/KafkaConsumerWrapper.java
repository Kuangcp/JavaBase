package com.github.kuangcp.wrapper.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.kuangcp.wrapper.Message;
import com.github.kuangcp.wrapper.config.KafkaConfigManager;
import java.time.Duration;
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

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:35
 */
@SuppressWarnings("unchecked")
@Slf4j
public class KafkaConsumerWrapper {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static volatile boolean stop = false;

  private static ExecutorService pool = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static Properties properties = KafkaConfigManager.getConsumerConfig()
      .orElseThrow(() -> new RuntimeException("加载Kafka消费者配置出错"));

  private static KafkaConsumer consumer;

  static {
    SimpleModule module = new SimpleModule();

    Runtime.getRuntime().addShutdownHook(new Thread(KafkaConsumerWrapper::shutdown));
  }

  // TODO 两个线程分摊 Topics？


  /**
   * 消费Topic
   *
   * @param duration 拉取间隔
   * @param executors 执行器
   * @param <E> executor
   */
  static <E extends MessageExecutor<String> & MessageTopic> void consumerPlainText(
      Duration duration, List<E> executors) {
    if (Objects.isNull(duration) || Objects.isNull(executors) || executors.isEmpty()) {
      log.warn("consumer param invalid");
      return;
    }

    if (Objects.nonNull(consumer)) {
      log.warn("consumer has started");
      return;
    }

    Map<String, E> executorMap = executors.stream()
        .collect(Collectors.toMap(E::getTopic, Function.identity(), (front, current) -> current));

    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(executorMap.keySet());
    while (!stop) {
      ConsumerRecords<String, String> records = consumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        E executor = executorMap.get(record.topic());

        if (Objects.nonNull(executor)) {

          pool.submit(() -> executor.execute(record.value()));
        }
      }
    }
  }

  /**
   * 消费Topic
   *
   * @param duration 拉取间隔
   * @param executors 执行器
   * @param <T> Message content 类型
   */
  static <T> void consumer(Duration duration, List<GeneralMessageExecutor<T>> executors) {
    if (Objects.isNull(duration) || Objects.isNull(executors) || executors.isEmpty()) {
      log.warn("consumer param invalid");
      return;
    }

    if (Objects.nonNull(consumer)) {
      log.warn("consumer has started");
      return;
    }

    Map<String, GeneralMessageExecutor<T>> executorMap = executors.stream()
        .collect(Collectors.toMap(GeneralMessageExecutor::getTopic, Function.identity(),
            (front, current) -> current));

    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(executorMap.keySet());
    while (!stop) {
      ConsumerRecords<String, String> records = consumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        GeneralMessageExecutor<T> executor = executorMap.get(record.topic());

        if (Objects.nonNull(executor)) {
          pool.submit(() -> {
            try {
              Message<T> message = objectMapper.readValue(record.value(), Message.class);

              // 因为以上反序列化无法获取泛型的类型，所以无法正确的设置 content 对象，根本原因是Java的泛型擦除
              // 解决方案是先将content再序列化 最后指定类型反序列化
              Class<T> contentClass = executor.getContentClass();
              String value = objectMapper.writeValueAsString(message.getContent());
              T content = objectMapper.readValue(value, contentClass);
              message.setContent(content);

              executor.execute(message);
            } catch (Exception e) {
              log.error("", e);
            }
          });

        }
      }
    }
  }

  /**
   * 消费者资源回收 线程池关闭
   */
  private static void shutdown() {
    stop = true;
    try {
      if (Objects.nonNull(consumer)) {
        consumer.close();
      }
      pool.shutdown();
    } catch (Exception e) {
      log.error("shutdown messageProducer error");
    }
  }
}
