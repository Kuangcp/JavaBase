package com.github.kuangcp.reference;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

/**
 * @author kuangcp
 */
public class StackReferenceTest {

  @Test
  public void testAvoidStackReference() {
    StopWatch started = StopWatch.createStarted();
    for (int i = 0; i < 1000000; i++) {
      Apple apple = new Apple("who");
      apple.toString(); // avoid jvm delete
    }
    started.stop();
    System.out.println(started);

    // 减少栈对象
    Apple apple;
    started = StopWatch.createStarted();
    for (int i = 0; i < 1000000; i++) {
      apple = new Apple("who");
      apple.toString();
    }
    started.stop();
    System.out.println(started);
  }

}
