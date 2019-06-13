package com.github.kuangcp.situation.asm.cglib;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-06-13 09:13
 */
@Slf4j
public class SimpleTransformerTest {

  @Before
  public void setUp() {

  }

  @Test
  public void testInit() {
    SimpleTransformer instance = SimpleTransformer.INSTANCE;
    log.info("instance={}", instance);
  }
}