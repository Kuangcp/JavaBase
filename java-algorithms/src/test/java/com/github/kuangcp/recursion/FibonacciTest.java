package com.github.kuangcp.recursion;

import com.github.kuangcp.time.GetRunTime;
import java.util.function.Function;
import org.junit.Test;

/**
 * @author kuangcp on 3/28/19-9:29 PM
 */
public class FibonacciTest {

  @Test
  public void testPerformance() {
    GetRunTime run = new GetRunTime().startCount();
    for (int i = 0; i < 20; i++) {
      Fibonacci.recursiveOne(i);
    }
    run.endCount();

    run.startCount();
    for (int i = 0; i < 100000; i++) {
      Fibonacci.loopOne(i);
    }
    run.endCount();

    run.startCount();
    for (int i = 0; i < 100000; i++) {
      Fibonacci.generalTermFormula(i);
    }
    run.endCount();
  }

  @Test
  public void testRecursiveOne() throws Exception {
    general(Fibonacci::recursiveOne, 10);
  }

  @Test
  public void testLoopOne() {
    general(Fibonacci::loopOne, 10);
  }

  @Test
  public void testGeneralTermFormula() {
    general(Fibonacci::generalTermFormula, 10);
  }

  private void general(Function<Integer, Integer> function, int sum) {
    for (int i = 0; i < sum; i++) {
      System.out.print(function.apply(i) + " ");
    }
    System.out.println();
  }
}