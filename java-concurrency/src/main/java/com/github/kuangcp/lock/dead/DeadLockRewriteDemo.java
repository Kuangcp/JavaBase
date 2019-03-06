package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午10:23
 * 使用并发包重写死锁案例：
 * 但是使用的时候，似乎lock方法会一直尝试获得锁，睡眠时间决定了死锁状况的长短（这应该不算死锁，只是等待）准备到确认的延时恰好是睡眠时间
 * 根据 API 知道这个lock如果发现资源被别的线程锁住，他就会休眠，等待锁的释放，所以这里的写法是不会有死锁的，可能7和8的区别？书上用的是7，说的是死锁
 */
public class DeadLockRewriteDemo {

  public static void main(String[] s) {

    DeadLockRewrite one = new DeadLockRewrite("one");
    DeadLockRewrite other = new DeadLockRewrite("other");
    Food fooda = new Food("a");
    Food foodb = new Food("b");

    new Thread(() -> one.prepareRun(fooda, other)).start();

    new Thread(() -> other.prepareRun(foodb, one)).start();
  }
}

