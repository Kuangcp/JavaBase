package com.github.kuangcp.hi;

import static com.github.kuangcp.hi.Constants.HI_TOPIC;
import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;
import static com.github.kuangcp.hi.Constants.START_TOPIC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.hi.domain.ProductStatisticJobCommand;
import com.github.kuangcp.hi.domain.ProductStatisticSpan;
import com.github.kuangcp.hi.domain.StartCommand;
import com.github.kuangcp.io.ResourceTool;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.UUID;
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

  private static Properties properties = getConf();
  private static ObjectMapper mapper = new ObjectMapper();

  static void sendHi() {
    Producer<String, String> producer = null;
    try {
      producer = new KafkaProducer<>(properties);
      for (int i = 0; i < 100; i++) {
        String msg = "Message " + i;
        producer.send(new ProducerRecord<>(HI_TOPIC, msg));
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

  static void sendStart() {
    Producer<String, String> producer = null;
    try {
      producer = new KafkaProducer<>(properties);
      for (int i = 0; i < 20; i++) {
        StartCommand msg = StartCommand.builder().place("There").scale(i).startTime(new Date())
            .build();
        producer.send(new ProducerRecord<>(START_TOPIC, mapper.writeValueAsString(msg)));
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

  static void sendCommand() {
    Producer<String, String> producer = null;
    HashSet<ProductStatisticSpan> spans = new HashSet<>();
    spans.add(ProductStatisticSpan.MONTH);

    try {
      producer = new KafkaProducer<>(properties);
      for (int i = 0; i < 2; i++) {
        ProductStatisticJobCommand msg = ProductStatisticJobCommand.builder()
            .id(UUID.randomUUID().toString() + "_test_" + i)
            .startTime(toDate(LocalDateTime.now().withMonth(1)))
            .endTime(toDate(LocalDateTime.now().withMonth(3)))
            .productStatisticSpan(spans)
            .build();
        ProducerRecord<String, String> record = new ProducerRecord<>(Constants.COMMAND_TOPIC,
            mapper.writeValueAsString(msg));

        long time = System.currentTimeMillis();
        producer.send(record, (metadata, e) -> {
          long elapsedTime = System.currentTimeMillis() - time;
          if (metadata != null) {
            log.info("sent record(key={} value={}) meta(partition={}, offset={}) time={}",
                record.key(), record.value(), metadata.partition(),
                metadata.offset(), elapsedTime);
          } else {
            log.error(e.getMessage(), e);
          }
        });
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

  public static Date toDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
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
