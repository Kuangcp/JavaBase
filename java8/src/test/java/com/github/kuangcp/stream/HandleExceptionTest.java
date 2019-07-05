package com.github.kuangcp.stream;

import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-10-下午4:14
 */
public class HandleExceptionTest {

  // NPE in stream
  @Test(expected = NullPointerException.class)
  public void testException() {
    IntStream.rangeClosed(1, 10).forEach(s -> {
      System.out.println(s);
      if (s > 7) {
        throw new NullPointerException("Oops ");
      }
    });
  }
}
