package com.github.kuangcp.hi;

import java.io.IOException;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-21 16:20
 */
public class ConsumerDemoTest {

  @Test
  public void testReceiveHi() {
    ConsumerDemo.receiveHi();
  }


  @Test
  public void testReceiveCommand() throws IOException {
    ConsumerDemo.receiveCommand();
  }
}
