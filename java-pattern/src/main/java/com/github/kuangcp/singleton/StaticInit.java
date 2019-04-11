package com.github.kuangcp.singleton;

/**
 * 懒汉式(线程不安全)[不可用]
 *
 * @author kuangcp on 2019-04-11 12:39 PM
 */
public class StaticInit {

  private static StaticInit singleton;

  private StaticInit() {
  }

  public static StaticInit getInstance() {
    if (singleton == null) {
      singleton = new StaticInit();
    }
    return singleton;
  }
}
