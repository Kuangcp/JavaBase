package com.github.kuangcp.countlatch;

import java.util.concurrent.CountDownLatch;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-7:56 AM
 */
class ProcessingThread extends Thread {

  private final String ids;
  private final CountDownLatch latch;

  public ProcessingThread(String id, CountDownLatch latch) {
    this.ids = id;
    this.latch = latch;
  }

  public void init() {
    // 节点初始化
    latch.countDown();
  }

  public void run() {
    init();
    System.out.println(this.ids);
  }
}
