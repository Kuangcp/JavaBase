package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:03 AM
 */
class DeadLockHandler {

  private final String id;
  private final Lock locks = new ReentrantLock();

  public DeadLockHandler(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void preparRun(Food food, DeadLockHandler lock) {
    boolean required = false;
    boolean done = false;

    while (!done) {
      int wait = (int) (Math.random() * 10);
      try {
//                Thread.sleep(2000);
        required = locks.tryLock(wait, TimeUnit.MILLISECONDS); // 尝试与锁定，超时时长随机
        if (required) {
          System.out.println(
              "准备 currentId: " + id + " resource: " + food.getName() + " prepar other: " + lock
                  .getId() + System.currentTimeMillis());
          done = lock.confirmRun(food, this); // 检查返回值
        } else {
          Thread.sleep(wait);
        }
      } catch (InterruptedException e) {
      } finally {
        if (done) {
          locks.unlock(); // 如果done为假，释放锁并等待
        }
      }
      if (!done) {
        try {
          Thread.sleep(wait);
        } catch (InterruptedException e) {
        }
      }
    }
  }

  public boolean confirmRun(Food food, DeadLockHandler lock) {
    boolean required = false;
    try {
      int wait = (int) (Math.random() * 10);
      required = locks.tryLock(wait, TimeUnit.MILLISECONDS);
      if (required) {
        System.out.println(
            "确认 currentId: " + id + " resource: " + food.getName() + "confirm other: " + lock
                .getId() + System.currentTimeMillis());
        return true;
      }
    } catch (InterruptedException e) {
    } finally {
      if (required) {
        locks.unlock();
      }
    }
    return false;
  }

}
