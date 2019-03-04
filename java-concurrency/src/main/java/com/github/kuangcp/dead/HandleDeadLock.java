package com.github.kuangcp.dead;

import com.github.kuangcp.old.Food;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午9:16
 * 解决死锁:
 * 如果在取得第二个锁的时候失败了，就将第一个锁释放掉，从头开始等待
 * 但是还是会有问题，并不是书上说的那么美好，如果遇到锁就释放，然后等待，两个线程都是这样的情况，
 * 然后就不停的释放，等待，加锁，死锁在了各自的第一个方法上，控制台就不停的刷准备方法的内容
 */
public class HandleDeadLock {

  public static void main(String[] s) {

    DeadLockHandle one = new DeadLockHandle("one");
    DeadLockHandle other = new DeadLockHandle("other");
    Food fooda = new Food("a");
    Food foodb = new Food("b");

    new Thread(new Runnable() {
      @Override
      public void run() {
        one.preparRun(fooda, other);
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        other.preparRun(foodb, one);
      }
    }).start();
  }
}

