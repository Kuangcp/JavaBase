package com.github.kuangcp.dead;

import com.github.kuangcp.old.Food;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午8:58
 * 尝试 解决死锁
 * 当遇到锁进行休眠，但是没有释放已经占用的锁，所以还是很大几率死锁
 */
public class DeadLockDemo {

  public static void main(String[] s) {

    DeadLocks one = new DeadLocks("one");
    DeadLocks other = new DeadLocks("other");
    Food fooda = new Food("a");
    Food foodb = new Food("b");

    new Thread(() -> one.prepareRun(fooda, other)).start();
    new Thread(() -> other.prepareRun(foodb, one)).start();
  }
}