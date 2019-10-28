package com.github.kuangcp.time.wheel;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-10-27 12:15
 */
@Slf4j
public class TimeWheelTest {

  private TimeWheel timeWheel = new TimeWheel(3000L, true);

  @Test
  public void testAdd() throws Exception {
    boolean result = timeWheel.add("id", () -> null, Duration.ofMillis(10000));
    Assert.assertTrue(result);
  }

  @Test
  public void testRun1() {
    for (int i = 0; i < 7; i++) {
      boolean result = timeWheel.add("id" + i, () -> null, Duration.ofMillis(i * 3000));
      log.debug(": result={}", result);
    }
    timeWheel.start();
    join();
  }

  private void join() {
    try {
      Thread.currentThread().join();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Test
  public void testRun2() {
    for (int i = 0; i < 13; i++) {
      boolean result = timeWheel.add("id" + i, () -> "fds", Duration.ofMillis(5000));
      log.info(": result={}", result);
    }
    timeWheel.start();

    try {
      TimeUnit.SECONDS.sleep(10);
      timeWheel.shutdown();
    } catch (Exception e) {
      log.error("", e);
    }
  }


  private long calculate() {
    long sum = 0;
    for (int i = 0; i < 10000000; i++) {
      Math.sqrt(sum);
      sum += i;
    }
    return sum;
  }

  @Test
  public void testRushLoop() {
    for (int i = 1; i < 7; i++) {
      boolean result = timeWheel.add("id" + i, this::calculate, Duration.ofMillis(i * 10000));
      log.info(": result={}", result);
    }
    timeWheel.printWheel();
    timeWheel.start();

    new Thread(() -> {
      while (true) {
        timeWheel.printWheel();
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          log.error("", e);
        }
      }
    }).start();
    join();
  }

}
