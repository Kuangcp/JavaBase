package com.github.kuangcp.wrapper.consumer;

import java.time.Duration;
import java.util.Collections;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-17 14:54
 */
public class KafkaConsumerWrapperTest {

  @Test
  public void testConsumer(){
    KafkaConsumerWrapper.consumer(Duration.ofMillis(1000), Collections.singleton("Hi"));
  }
}
