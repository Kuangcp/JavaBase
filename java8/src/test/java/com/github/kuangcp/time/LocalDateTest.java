package com.github.kuangcp.time;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-07-09 17:10
 */
@Slf4j
public class LocalDateTest {

  @Test
  public void testCompare() {
    LocalDate now = LocalDate.now();
    for (int i = 1; i < 10; i++) {
      LocalDate newDate = now.plusDays(i);
      boolean isAfter = newDate.isAfter(LocalDate.of(2019, 7, 15));

      log.info("newDate={} isAfter={}", newDate, isAfter);
    }
  }
}
