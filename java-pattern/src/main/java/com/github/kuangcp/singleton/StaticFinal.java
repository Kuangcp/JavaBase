package com.github.kuangcp.singleton;

/**
 * 饿汉式（静态常量）
 * @author kuangcp on 2019-04-11 12:37 PM
 */
public class StaticFinal {

  private static final StaticFinal instance = new StaticFinal();

  private StaticFinal() {
  }

  public static StaticFinal getInstance() {
    return instance;
  }
}
