package com.github.kuangcp.lock.sync;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/7/19-1:09 AM
 */
@Slf4j
public class SleepDemo {

  @Slf4j
  static class Tool implements Runnable {

    private String name;

    public Tool setName(String name) {
      this.name = name;
      return this;
    }

    @Override
    public void run() {
      while (true) {
        synchronized (this) {
          try {
            log.info("running: name={}", name);
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // 结果是 0 1 无序输出, 因为线程先后的调度不是完美的, 如果是完美的, 那么就会一直输出0 1 永远拿不到锁
  public static void main(String[] args) {
    new Thread(new Tool().setName("0")).start();
    new Thread(new Tool().setName("1")).start();

    new Thread(() -> {
      while (true) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println();
      }
    }).start();
  }
}
