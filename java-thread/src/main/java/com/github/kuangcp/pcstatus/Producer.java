package com.github.kuangcp.pcstatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:01
 *
 * @author kuangcp
 */
@Slf4j
@AllArgsConstructor
public class Producer extends Thread {

  private Share shared;
  private int number;

  public void run() {
    for (int i = 0; i < 10; i++) {
      shared.put(i);
      log.info("生产者 {}  生产的数据为: {}", this.number, i);
      try {
        sleep((int) (Math.random() * 100));
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
