package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-7:52 AM
 */
@Slf4j
class DeadLock {

  private final String id;
  private final Lock locks = new ReentrantLock();

  DeadLock(String id) {
    this.id = id;
  }

  private String getId() {
    return id;
  }

  void prepareRun(Food food, DeadLock lock) {
    boolean required = false;
    while (!required) {
      try {
        int wait = (int) (Math.random() * 10);
        required = locks.tryLock(wait, TimeUnit.MILLISECONDS); // 尝试与锁定，超时时长随机
        if (required) {
          log.info("prepare: currentId={} resource={} lock={} time={}",
              id, food.getName(), lock.getId(), System.currentTimeMillis());

          lock.confirmRun(food, this); // 给主线程确认
        } else {
          Thread.sleep(wait);
        }

      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      } finally {
        if (required) {
          locks.unlock(); // 如果锁定就解锁
        }
      }
    }
  }

  private void confirmRun(Food food, DeadLock lock) {
    locks.lock(); // 尝试锁住其他线程 正是这里可能出现死锁，因为这个其他线程已经加锁这里就死锁了
    try {
      log.info("run: currentId={} resource={} lock={} time={}",
          id, food.getName(), lock.getId(), System.currentTimeMillis());
    } finally {
      locks.unlock();
    }
  }
}