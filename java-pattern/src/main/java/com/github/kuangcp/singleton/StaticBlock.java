package com.github.kuangcp.singleton;

/**
 * 饿汉式（静态代码块）
 *
 * @author kuangcp on 2019-04-11 12:38 PM
 */
public class StaticBlock {

  private static StaticBlock instance;

  static {
    instance = new StaticBlock();
  }

  private StaticBlock() {
  }

  public static StaticBlock getInstance() {
    return instance;
  }
}
