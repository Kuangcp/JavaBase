package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午9:16
 * 解决死锁:
 * 如果在取得第二个锁的时候失败了，就将第一个锁释放掉，从头开始等待
 * 但是还是会有问题，并不是书上说的那么美好，如果遇到锁就释放，然后等待，两个线程都是这样的情况，
 * 然后就不停的释放，等待，加锁，死锁在了各自的第一个方法上，控制台就不停的刷准备方法的内容
 */
public class DeadLockHandlerDemo {

  public static void main(String[] s) {

    DeadLockHandler one = new DeadLockHandler("one");
    DeadLockHandler other = new DeadLockHandler("other");
    Food foodA = new Food("a");
    Food foodB = new Food("b");

    new Thread(() -> one.preparRun(foodA, other)).start();
    new Thread(() -> other.preparRun(foodB, one)).start();
  }
}

