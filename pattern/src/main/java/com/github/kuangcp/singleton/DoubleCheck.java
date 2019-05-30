package com.github.kuangcp.singleton;

/**
 * 双重校验方式  线程不安全
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
        // 因为 new 这个操作是分为三步
        // 1.分配内存空间, 2.初始化对象, 3.singleton 引用变量指向内存空间
        // 2 3 步可能发生指令重排序

        // 线程A执行1、3后让出cpu，此时还未执行2，别的线程拿到cpu，发现instance不为null，直接返回使用，而此时 instance还未初始化。

        singleton = new DoubleCheck();
      }
    }
    return singleton;
  }
}
