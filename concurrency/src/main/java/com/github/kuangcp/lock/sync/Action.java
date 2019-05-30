package com.github.kuangcp.lock.sync;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-22 6:33 PM
 */
@Slf4j
public class Action {

  private Object lock = new Object();

  synchronized void syncMethod() {
    log.info("invoke syncMethod");
    sleep(100);
    log.info("exit syncMethod");
  }

  synchronized void syncMethod2() {
    log.info("invoke syncMethod2");
    sleep(200);
    log.info("exit syncMethod2");
  }

  void syncMethodWithLock() {
    synchronized (lock) {
      log.info("invoke syncMethodWithLock");
      sleep(100);
      log.info("exit syncMethodWithLock");
    }
  }

  void syncMethodWithLock2() {
    synchronized (lock) {
      log.info("invoke syncMethodWithLock2");
      sleep(200);
      log.info("exit syncMethodWithLock2");
    }
  }

  private void sleep(int mills) {
    try {
      TimeUnit.MILLISECONDS.sleep(mills);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
