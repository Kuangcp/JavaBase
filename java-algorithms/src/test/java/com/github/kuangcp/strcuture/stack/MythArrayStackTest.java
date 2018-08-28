package com.github.kuangcp.strcuture.stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-29-上午12:03
 */
public class MythArrayStackTest {

  //Field elementData of type Object[] - was not mocked since Mockito doesn't mock arrays
  private MythArrayStack<Integer> stack = new MythArrayStack<>();

  @Test
  public void testPush() {
    stack.push(1212);
    assertThat(stack.size(), equalTo(1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testPop() {
    int result = stack.pop();
    assertThat(result, equalTo(null));
  }

  @Test
  public void testPop2() {
    testPush();
    int result = stack.pop();
    assertThat(result, equalTo(1212));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testPeek() {
    int result = stack.peek();
    Assert.assertEquals(1, result);
  }

  @Test
  public void testPeek2() {
    testPush();
    Integer result = stack.peek();
    assertThat(result, equalTo(result));
  }

  @Test
  public void testIsEmpty() {
    boolean result = stack.isEmpty();
    Assert.assertTrue(result);

    testPush();
    assertThat(stack.isEmpty(), equalTo(false));
  }

  @Test
  public void testIsFull() {
    boolean result = stack.isFull();
    assertThat(result, equalTo(false));

    for (int i = 0; i < 10; i++) {
      stack.push(i);
    }
    result = stack.isFull();
    assertThat(result, equalTo(true));

  }

  @Test
  public void testClear() {
    testIsFull();
    assertThat(stack.isFull(), equalTo(true));

    stack.clear();
    assertThat(stack.isEmpty(), equalTo(true));
  }
}
