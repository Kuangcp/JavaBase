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
  public void testConvert(){

    Duration duration = Duration.ofHours(23);
    duration = duration.plus(59, ChronoUnit.MINUTES);
    log.info("duration={}", duration.toMinutes());
  }
}
