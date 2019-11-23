package com.github.kuangcp.wrapper.config;

import java.util.Optional;
import java.util.Properties;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-17 14:45
 */
public class KafkaConfigManagerTest {

  @Test
  public void testConsumer(){
    Optional<Properties> propOpt = KafkaConfigManager.getConsumerConfig();

    propOpt.ifPresent(System.out::println);
  }
}
