package com.github.kuangcp.hi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.hi.domain.ProductStatisticJobCommand;
import com.github.kuangcp.kafka.KafkaConsumerUtil;
import com.github.kuangcp.kafka.common.SimpleMessageExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.github.kuangcp.hi.Constants.HI_TOPIC;
import static com.github.kuangcp.hi.Constants.KAFKA_SERVER;

/**
 * @author kuangcp
 * <p>
 * Date: 2019-05-20 00:11
 */
@Slf4j
public class ConsumerDemo {

    private static Properties properties = getConf();
    private static ObjectMapper mapper = new ObjectMapper();

    static void receiveHi() {
        SimpleMessageExecutor executor = new SimpleMessageExecutor() {
            @Override
            public void execute(String message) {
                log.info("message={}", message);
            }

            @Override
            public String getTopic() {
                return HI_TOPIC;
            }
        };

        KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(1000),
                Collections.singletonList(executor));
    }

    static void receiveCommand() throws IOException {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(Constants.COMMAND_TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                log.info("offset = {}, value = {}", record.offset(), record.value());
                ProductStatisticJobCommand command = mapper
                        .readValue(record.value(), ProductStatisticJobCommand.class);
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