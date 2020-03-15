package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:03 AM
 */
@Slf4j
class DeadLockRewrite implements MythDeadLockAble {

  private final String id;
  private final Lock locks = new ReentrantLock();

  DeadLockRewrite(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void prepareRun(Food food, MythDeadLockAble lock) {
    locks.lock(); // 线程先锁住自己的锁
    try {
      log.info("{} 准备 currentId: {} resource: {} prepare other: {}",
          System.currentTimeMillis(), id, food.getName(), lock.getId());
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } finally {
      locks.unlock();
    }
    lock.confirmRun(food, this);
  }

  public void confirmRun(Food food, MythDeadLockAble lock) {
    locks.lock(); // 尝试锁住其他线程 正是这里可能出现死锁，因为这个其他线程已经加锁这里就死锁了
    try {
      log.info("{} 确认 currentId: {} resource: {} confirm other: {}",
          System.currentTimeMillis(), id, food.getName(), lock.getId());
    } finally {
      locks.unlock();
    }
  }

  @Override
  public boolean confirmRun2(Food food, MythDeadLockAble lock) {
    return false;
  }
}
