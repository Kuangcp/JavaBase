package com.github.kuangcp.strcuture.stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-28-下午11:24
 */
public class MythLinkedStackTest {

  private MythBaseStack<Integer> stack = new MythLinkedStack<>();

  @Test
  public void testPush() {
    stack.push(1212);
    assertThat(stack.size(), equalTo(1));
  }

  @Test
  public void testPop() throws Exception {
    testPush();
    Integer pop = stack.pop();
    assertThat(pop, equalTo(1212));
  }

  @Test
  public void testPeek() throws Exception {
    testPush();
    assertThat(stack.peek(), equalTo(1212));
  }

  @Test
  public void testLength() throws Exception {
    int result = stack.size();
    Assert.assertEquals(0, result);
    testPush();
    assertThat(stack.size(), equalTo(1));
  }

  @Test
  public void testIsEmpty() {
    boolean result = stack.isEmpty();
    Assert.assertTrue(result);
    testPush();
    assertThat(stack.isEmpty(), equalTo(false));
  }

  @Test
  public void testClear() {
    testIsEmpty();
    stack.clear();
    assertThat(stack.size(), equalTo(0));
  }
}
