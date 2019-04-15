package com.github.kuangcp.order;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:54
 * 多线程有序的输出结果  参考博客 http://www.jb51.net/article/108762.htm
 * 在Junit中, test方法来测试多线程, 因为一个Test注解的方法看做main方法的时候, 没有阻塞的,所以直接退出了
 * 里面写的开启的线程都是隶属于他的子线程, 所以也一起跟着关闭了.
 * 思路:
 *
 * @author kuangcp
 */
public class OrderedThreadTest {

  private static AtomicInteger count = new AtomicInteger(0);

  @Test
  public void testOrder() throws InterruptedException {
    Thread thread1 = new Thread(new Task("A", 0, count));
    Thread thread2 = new Thread(new Task("B", 1, count));
    Thread thread3 = new Thread(new Task("C", 2, count));

    thread1.start();
    thread2.start();
    thread3.start();

    // 避免Junit主线程直接退出
    thread1.join();
    thread2.join();
    thread3.join();
  }

  @Test
  public void testOrderWithVolatile() throws InterruptedException {
    Thread thread1 = new Thread(new TaskWithVolatile("A", 0));
    Thread thread2 = new Thread(new TaskWithVolatile("B", 1));
    Thread thread3 = new Thread(new TaskWithVolatile("C", 2));

    thread1.start();
    thread2.start();
    thread3.start();

    // 避免Junit主线程直接退出
    thread1.join();
    thread2.join();
    thread3.join();
  }
}