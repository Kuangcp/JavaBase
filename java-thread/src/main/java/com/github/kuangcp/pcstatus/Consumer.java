package com.github.kuangcp.pcstatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 18-1-4  下午12:02
 * 消费者
 *
 * @author kuangcp
 */
@Slf4j
@AllArgsConstructor
public class Consumer extends Thread {

  private Share shared;
  private int number;

  public void run() {
    int value;
    for (int i = 0; i < 10; i++) {
      value = shared.get();
      log.info("消费者 {}  消费的数据为: {}", this.number, value);
    }
  }
}
