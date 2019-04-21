package com.github.kuangcp.lock.pvp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

/**
 * @author kuangcp on 2019-04-21 11:14 AM
 */
public class PlayerTest {

  private ReentrantLock lock = new ReentrantLock();

  // TODO  锁
  @Test
  public void testRead() throws InterruptedException {

    Thread thread = new Thread(() -> {
      for (int i = 0; i < 40; i++) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        lock.lock();
      }
    });
    thread.setDaemon(true);
    thread.start();

    TimeUnit.SECONDS.sleep(2);
    try {
      // 如果这里获取锁失败, finally 里又去释放锁, 会得到IllegalMonitorStateException, 掩盖了这里获取锁的异常
      lock.lock();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }
}
