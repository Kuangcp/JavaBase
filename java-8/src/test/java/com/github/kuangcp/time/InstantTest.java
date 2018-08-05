package com.github.kuangcp.time;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-4-下午11:40
 */
@Slf4j
public class InstantTest {

  @Test
  public void testInstant() {
    Instant instant = Instant.now();
    System.out.println(instant);
  }

}
