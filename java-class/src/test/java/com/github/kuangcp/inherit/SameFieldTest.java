package com.github.kuangcp.inherit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Test;

/**
 * RTTI C++ 中的概念, Java中没有, 但是 Thinking in java 里面用了这个概念, 实际上是有歧义的
 * 多态是隐式地利用RTTI，反射则是显式地使用RTTI
 *
 * 运行时类型识别(RTTI, Run-Time Type Identification)是Java中非常有用的机制，在Java运行时，RTTI维护类的相关信息。
 * 多态(polymorphism)是基于RTTI实现的。RTTI的功能主要是由Class类实现的。
 *
 * @author kuangcp on 2019-04-11 12:54 PM
 */
public class SameFieldTest {

  public static class A {

    int age = 1;
  }

  public static class B extends A {

    int age = 3;
  }

  @Test
  public void testSame() {
    A a = new B();
    B b = new B();

    System.out.println(a.getClass());
    System.out.println(b.getClass());

    // TODO ?
    assertThat(a.age + b.age, equalTo(4));
  }
}
