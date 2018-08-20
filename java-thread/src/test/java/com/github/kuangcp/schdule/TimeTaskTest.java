package com.github.kuangcp.schdule;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Timer;
import java.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author kuangcp on 18-8-20-下午3:20
 */
@Slf4j
public class TimeTaskTest {

  @Test
  public void testTask() throws InterruptedException {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("2");
      }
    }, 1000);

    Thread.sleep(4000);
  }

  @Test
  public void testLoop() {
    double i;
    int count = 0;
    for (i = 0; i != 10; i += 0.1) {
      count++;
      if (count > 1000) {
        break;
      }
      System.out.printf(" %.1f \n", i);
    }
    log.debug("result: i={}, count={}", i, count);
    assertThat(i, equalTo(10));
  }

  @Test
  public void testEqual() {
    assertThat(3.0 - 0.1222, equalTo(2.8778));
  }

}
