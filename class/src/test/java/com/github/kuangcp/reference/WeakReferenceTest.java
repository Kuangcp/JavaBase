package com.github.kuangcp.reference;

import java.lang.ref.WeakReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-09-24 12:35
 * @see java.lang.ThreadLocal.ThreadLocalMap.Entry
 */
@Slf4j
public class WeakReferenceTest {

  @Test
  public void testClear() throws InterruptedException {
    WeakReference<Apple> weakReference = new WeakReference<>(new Apple("红富士"));
    //通过WeakReference的get()方法获取Apple
    log.info("Apple={}", weakReference.get());

    System.gc();

    //休眠一下，在运行的时候加上虚拟机参数-XX:+PrintGCDetails，输出gc信息，确定gc发生了。
    Thread.sleep(2000);

    //如果为空，代表被回收了
    Assert.assertNull(weakReference.get());
    log.info("apple has clear");
  }

  @Test
  public void testHold() throws InterruptedException {
    Apple apple = new Apple("红富士");
    WeakReference<Apple> weakReference = new WeakReference<>(apple);

    //通过WeakReference的get()方法获取Apple
    log.info("Apple={}", weakReference.get());

    System.gc();

    //休眠一下，在运行的时候加上虚拟机参数-XX:+PrintGCDetails，输出gc信息，确定gc发生了。
    Thread.sleep(2000);

    Assert.assertNotNull(weakReference.get());
    log.info("apple hasn't clear");
  }
}

