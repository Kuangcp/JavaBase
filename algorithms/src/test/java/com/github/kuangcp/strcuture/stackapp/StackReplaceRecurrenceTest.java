package com.github.kuangcp.strcuture.stackapp;

import java.util.HashMap;
import java.util.Map.Entry;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author https://github.com/kuangcp on 2019-11-19 21:38
 */


public class StackReplaceRecurrenceTest {

  @Test
  public void testCalculate() throws Exception {
    HashMap<Integer, Double> argAndResult = new HashMap<>();
    argAndResult.put(4, 8.0);
    argAndResult.put(30, 9450.0);

    for (Entry<Integer, Double> entry : argAndResult.entrySet()) {
      double result = StackReplaceRecurrence.calculateByStack(entry.getKey());
      assertThat(result, equalTo(entry.getValue()));

      double recursive = StackReplaceRecurrence.calculateByRecursive(entry.getKey());
      assertThat(recursive, equalTo(entry.getValue()));
    }
  }
}
