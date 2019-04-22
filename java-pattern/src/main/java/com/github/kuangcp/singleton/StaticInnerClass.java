package com.github.kuangcp.singleton;

/**
 * 静态内部类 [推荐用]
 *
 * @author kuangcp on 2019-04-11 12:42 PM
 */
public class StaticInnerClass {

  private StaticInnerClass() {
  }

  private static class SingletonInstance {

    private static final StaticInnerClass INSTANCE = new StaticInnerClass();
  }

  public static StaticInnerClass getInstance() {
    return SingletonInstance.INSTANCE;
  }

}
