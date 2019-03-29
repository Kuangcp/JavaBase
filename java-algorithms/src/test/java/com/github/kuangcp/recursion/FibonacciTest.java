package com.github.kuangcp.recursion;

import org.junit.Test;

/**
 * @author kuangcp on 3/28/19-9:29 PM
 */
public class FibonacciTest {

  @Test
  public void testCalculate() throws Exception {
    Fibonacci fibonacci = new Fibonacci();
    for (int i = 0; i < 10; i++) {
      int result = fibonacci.calculate(i);
      System.out.println(result);
    }
  }
}