package com.github.kuangcp.strcuture.stackapp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.github.kuangcp.strcuture.stack.MythBaseStack;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-27-上午12:36
 */
public class AddTwoBigIntegerTest {

  @Test
  public void testAdd() {
    AddTwoBigInteger addTwoBigInteger = new AddTwoBigInteger();
    MythBaseStack<Integer> result = addTwoBigInteger.add("112233", "332211");
    assertThat(stackToString(result), equalTo("444444"));

    addTwoBigInteger.clear();
    result = addTwoBigInteger.add("5367868436215345", "743558532109789079793128");
    assertThat(stackToString(result), equalTo("743558537477657516008473"));

    addTwoBigInteger.clear();
    result = addTwoBigInteger.add("0000001", "1");
    assertThat(stackToString(result), equalTo("2"));
  }

  private String stackToString(MythBaseStack stack) {
    StringBuilder sb = new StringBuilder();

    while (!stack.isEmpty()) {
      sb.append(stack.pop());
    }
    return sb.toString();
  }
}
