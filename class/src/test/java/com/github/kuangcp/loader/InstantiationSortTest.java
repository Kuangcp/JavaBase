package com.github.kuangcp.loader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-17 5:16 PM
 * 类属性和对象属性实例化的顺序
 *
 * 17:21:08.112 INFO  [main] c.g.k.i.InstantiationSortTest:21  object init block
 * 17:21:08.117 INFO  [main] c.g.k.i.InstantiationSortTest:25  constructor a=1 b=2
 * 17:21:08.120 INFO  [main] c.g.k.i.InstantiationSortTest:17  static init block
 * 17:21:08.120 INFO  [main] c.g.k.i.InstantiationSortTest:30  static method
 */
@Slf4j
public class InstantiationSortTest {

  static int b = 2;
  int a = 1;

  static InstantiationSortTest test = new InstantiationSortTest();

  static {
    log.info("static init block");
  }

  {
    log.info("object init block");
  }

  InstantiationSortTest() {
    log.info("constructor a={} b={}", a, b);
  }

  static void staticMethod() {
    log.info("static method");
  }

  public static void main(String[] args) {
    staticMethod();
  }
}
