package com.github.kuangcp.volatiles;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-24 9:02 PM
 */
@Slf4j
class NeverStopThread {

  private boolean stop = false;

  private boolean stopWithSleep = false;
  private volatile boolean stopWithVolatile = false;

  void neverStop() {
    while (!stop) {
    }

    log.info("exit neverStop");
  }

  void stop() {
    this.stop = true;
  }

  // JVM 会尽力保证内存的可见性，即便这个变量没有加同步关键字。换句话说，只要 CPU 有时间，JVM 会尽力去保证变量值的更新。
  // 这种与 volatile 关键字的不同在于，volatile 关键字会强制的保证线程的可见性。
  // 使用 System.out.println(); 或者其他语句都是有可能达到效果, 只要能让CPU空闲下来
  void normalStopWithSleep() {
    while (!stopWithSleep) {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
      } catch (InterruptedException e) {
        log.error("", e);
      }
      log.info("run with sleep");
    }

    log.info("exit normalStopWithSleep");
  }

  void stopWithSleep() {
    this.stopWithSleep = true;
  }

  // 由于CPU调度的不可控性, 所以并不会在 stop 执行后就立马停掉
  void normalStopWithVolatile() {
    while (!stopWithVolatile) {
    }

    log.info("exit normalStopWithVolatile");
  }

  // 因为这个关键字保证了可见性, 所以能使得线程停下来
  void stopWithVolatile() {
    this.stopWithVolatile = true;
  }
}
