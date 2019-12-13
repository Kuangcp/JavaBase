package com.github.kuangcp.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-10-27 11:43
 */
@Slf4j
public class DurationTest {

  @Test
  public void testConvert() {
    Duration duration = Duration.ofHours(23);
    duration = duration.plus(59, ChronoUnit.MINUTES);
    log.info("duration={}", duration.toMinutes());
  }

  @Test
  public void testNegative() {
    Duration duration = Duration.ofMillis(-1000);
    System.out.println(duration.getSeconds());
  }

  private String format(Duration duration) {
    return duration.toHours() + ":" + duration.toMinutes() + ":" + duration.getSeconds() % 60;
  }

  @Test
  public void testFormat() {
    for (int i = 0; i < 600; i++) {
      Duration duration = Duration.ofMillis(i * 300);
      log.info("{}", format(duration));
    }
  }
}
