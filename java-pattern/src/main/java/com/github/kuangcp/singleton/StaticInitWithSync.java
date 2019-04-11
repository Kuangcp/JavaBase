package com.github.kuangcp.singleton;

/**
 * 懒汉式(线程安全，同步方法)[不推荐用]
 *
 * @author kuangcp on 2019-04-11 12:40 PM
 */
public class StaticInitWithSync {

  private static StaticInitWithSync singleton;

  private StaticInitWithSync() {
  }

  public synchronized static StaticInitWithSync getInstance() {
    if (singleton == null) {
      singleton = new StaticInitWithSync();
    }
    return singleton;
  }
}
