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

  @Test
  public void testPushAndPop() {
    MythArrayStack<Integer> stack = new MythArrayStack<>(3);
    stack.push(1);
    stack.push(2);
    stack.push(3);
    
    assertThat(stack.size(), equalTo(3));
    assertThat(stack.pop(), equalTo(Integer.valueOf(3)));
    assertThat(stack.pop(), equalTo(Integer.valueOf(2)));
    assertThat(stack.pop(), equalTo(Integer.valueOf(1)));
    assertThat(stack.isEmpty(), equalTo(true));
  }

  @Test
  public void testAutoExpand() {
    MythArrayStack<Integer> stack = new MythArrayStack<>(2);
    // 测试扩容
    for (int i = 0; i < 5; i++) {
      stack.push(i);
    }
    assertThat(stack.size(), equalTo(5));
    
    // 验证数据正确性
    for (int i = 4; i >= 0; i--) {
      assertThat(stack.pop(), equalTo(Integer.valueOf(i)));
    }
  }

  @Test
  public void testPeekString() {
    MythArrayStack<String> stack = new MythArrayStack<>();
    stack.push("test");
    assertThat(stack.peek(), equalTo("test"));
    assertThat(stack.size(), equalTo(1)); // peek不应该移除元素
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testPopEmpty() {
    MythArrayStack<Integer> stack = new MythArrayStack<>();
    stack.pop(); // 应该抛出异常
  }

  @Test
  public void testClearStack() {
    MythArrayStack<Integer> stack = new MythArrayStack<>();
    stack.push(1);
    stack.push(2);
    stack.clear();
    assertThat(stack.isEmpty(), equalTo(true));
    assertThat(stack.size(), equalTo(0));
  }
}
