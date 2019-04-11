package com.github.kuangcp.singleton;

/**
 * 双重校验 (线程安全，同步代码块)[不可用]
 *
 * @author kuangcp on 2019-04-11 12:41 PM
 */
public class DoubleCheck {

  private static DoubleCheck singleton;

  private DoubleCheck() {
  }

  public static DoubleCheck getInstance() {
    if (singleton == null) {
      synchronized (DoubleCheck.class) {
        singleton = new DoubleCheck();
      }
    }
    return singleton;
  }
}
