package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-03-16 00:22
 *
 * 尝试 解决死锁
 * 当遇到锁进行休眠，但是没有释放已经占用的锁，所以还是很大几率死锁
 */
@Slf4j
public class DeadLockTest {

  @Test
  public void testDeadLock() throws Exception {
    DeadLock one = new DeadLock("one");
    DeadLock other = new DeadLock("other");
    commonLogic(one, other);
  }


  /**
   * 解决死锁:
   * 如果在取得第二个锁的时候失败了，就将第一个锁释放掉，从头开始等待
   * 但是还是会有问题，并不是书上说的那么美好，如果遇到锁就释放，然后等待，两个线程都是这样的情况，
   * 然后就不停的释放，等待，加锁，死锁在了各自的第一个方法上，控制台就不停的刷准备方法的内容
   */
  @Test
  public void testHandle() throws Exception {
    DeadLockHandler one = new DeadLockHandler("one");
    DeadLockHandler other = new DeadLockHandler("other");

    commonLogic(one, other);
  }

  /**
   * 使用并发包重写死锁案例：
   *
   * 但是使用的时候，似乎lock方法会一直尝试获得锁，睡眠时间决定了死锁状况的长短
   * （这应该不算死锁，只是等待）准备到确认的延时恰好是睡眠时间
   * 根据 API 知道这个lock如果发现资源被别的线程锁住，他就会休眠，等待锁的释放，所以这里的写法是不会有死锁的，
   * 可能7和8的区别？书上用的是7，说的是死锁
   */
  @Test
  public void testRewrite() throws Exception {
    DeadLockRewrite one = new DeadLockRewrite("one");
    DeadLockRewrite other = new DeadLockRewrite("other");
    commonLogic(one, other);
  }

  private void commonLogic(MythDeadLockAble one, MythDeadLockAble other) throws Exception {
    Food foodA = new Food("a");
    Food foodB = new Food("b");

    new Thread(() -> {
      one.prepareRun(foodA, other);
      log.info("end");
    }).start();

    new Thread(() -> {
      other.prepareRun(foodB, one);
      log.info("end");
    }).start();

    Thread.currentThread().join(60000);
    log.info("thread end");
  }
}
