package com.github.kuangcp.lock.sync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-22 6:38 PM
 */
@Slf4j
public class ActionTest {


  @Test
  public void testSyncMethod() throws InterruptedException {
    Action action = new Action();

    Thread thread0 = new Thread(() -> {
      action.syncMethod();
      action.syncMethod2();
    });
    Thread thread1 = new Thread(() -> {
      action.syncMethod();
      action.syncMethod2();
    });

    thread0.start();
    thread1.start();

    // 如果两个方法睡眠时间一致, 很有可能会一直出现 0 线程先执行完两个方法才轮到线程 1
    thread0.join();
    thread1.join();

//    while(Thread.activeCount() > 1){
//      Thread.yield();
//    }
  }
}
