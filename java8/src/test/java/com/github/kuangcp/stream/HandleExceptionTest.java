package com.github.kuangcp.stream;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-10-下午4:14
 */
public class HandleExceptionTest {

  // NPE in stream
  @Test(expected = NullPointerException.class)
  public void testException() {
    IntConsumer consumer = v -> {
      if (v > 3) {
        throw new NullPointerException("Oops " + v);
      } else {
        System.out.println(v);
      }
    };

    IntStream.rangeClosed(1, 5).forEach(consumer);
  }
}
