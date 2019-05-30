package com.github.kuangcp.hi;

import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-21 16:19
 */
public class ProducerDemoTest {

  @Test
  public void testSendCommand() {
    ProducerDemo.sendCommand();
  }

  @Test
  public void testSendStart() {
    ProducerDemo.sendStart();
  }

  @Test
  public void testSendHi() {
    ProducerDemo.sendHi();
  }
}