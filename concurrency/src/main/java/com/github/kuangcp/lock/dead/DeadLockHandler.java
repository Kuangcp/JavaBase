package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:03 AM
 */
@Slf4j
class DeadLockHandler implements MythDeadLockAble {

  private final String id;
  private final Lock lock = new ReentrantLock();

  public DeadLockHandler(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void prepareRun(Food food, MythDeadLockAble lock) {
    boolean required = false;
    boolean done = false;

    while (!done) {
      int wait = (int) (Math.random() * 10);
      try {
        required = this.lock.tryLock(wait, TimeUnit.MILLISECONDS); // 尝试与锁定，超时时长随机
        if (required) {
          log.info("{} 准备 currentId: {} resource: {} prepare other: {}",
              System.currentTimeMillis(), id, food.getName(), lock.getId());
          done = lock.confirmRun2(food, this); // 检查返回值
        } else {
          Thread.sleep(wait);
        }
      } catch (InterruptedException e) {
        log.error("", e);
      } finally {
        if (done) {
          this.lock.unlock(); // 如果done为假，释放锁并等待
        }
      }
      if (!done) {
        try {
          Thread.sleep(wait);
        } catch (InterruptedException e) {
          log.error("", e);
        }
      }
    }
  }

  @Override
  public void confirmRun(Food food, MythDeadLockAble lock) {

  }

  @Override
  public boolean confirmRun2(Food food, MythDeadLockAble lock) {
    boolean required = false;
    try {
      int wait = (int) (Math.random() * 10);
      required = this.lock.tryLock(wait, TimeUnit.MILLISECONDS);
      if (required) {
        log.info("{} 确认 currentId: {} resource: {} confirm other: {}",
            System.currentTimeMillis(), id, food.getName(), lock.getId());
        return true;
      }
    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      if (required) {
        this.lock.unlock();
      }
    }
    return false;
  }
}
