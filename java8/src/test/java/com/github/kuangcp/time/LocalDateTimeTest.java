package com.github.kuangcp.time;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 * Java8 中新类 LocalDateTime
 *
 * @author kuangcp
 */
@Slf4j
public class LocalDateTimeTest {


  @Test
  public void testGet() {
    LocalDateTime dateTime = LocalDateTime.now();
    log.debug("date: {}-{}-{} {}:{}:{}:{}", dateTime.getYear(), dateTime.getMonth(),
        dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond(),
        dateTime.getNano());

    log.debug("{} {}", dateTime.getDayOfWeek(), dateTime.getDayOfYear());
  }
}
