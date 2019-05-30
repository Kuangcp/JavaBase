package com.github.kuangcp.singleton;

/**
 * 懒汉式 (线程安全，同步方法) [不推荐用] 有额外的锁, 同步消耗
 *
 * @author kuangcp on 2019-04-11 12:40 PM
 */
public class StaticLazyWithSyncMethod {

  private static StaticLazyWithSyncMethod singleton;

  private StaticLazyWithSyncMethod() {
  }

  public synchronized static StaticLazyWithSyncMethod getInstance() {
    if (singleton == null) {
      singleton = new StaticLazyWithSyncMethod();
    }
    return singleton;
  }
}
