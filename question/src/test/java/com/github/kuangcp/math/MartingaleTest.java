package com.github.kuangcp.math;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * 2019-06-16 11:36
 */
@Slf4j
public class MartingaleTest {

  private Martingale martingale = new Martingale();

  @Test
  public void testMartingale() {
    long result = martingale.martingale(99999999L, 2000);
    log.info("result={}", result);
  }
}
