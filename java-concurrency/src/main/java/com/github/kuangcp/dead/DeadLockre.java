package com.github.kuangcp.dead;

import com.github.kuangcp.old.Food;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:03 AM
 */
class DeadLockre {
    private final String id;
    private final Lock locks = new ReentrantLock();

    DeadLockre(String id) {
        this.id = id;
    }

    private String getId() {
        return id;
    }
    void preparRun(Food food, DeadLockre lock){
        locks.lock(); // 线程先锁住自己的锁
        try {
            System.out.println("准备 currentId: " + id + " resource: " + food.getName() + " prepar other: " + lock.getId()+System.currentTimeMillis());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }finally {
            locks.unlock();
        }
        lock.confirmRun(food, this);
    }
    private void confirmRun(Food food, DeadLockre lock){
        locks.lock(); // 尝试锁住其他线程 正是这里可能出现死锁，因为这个其他线程已经加锁这里就死锁了
        try {
            System.out.println("确认 currentId: " + id + " resource: " + food.getName() + "confirm other: " + lock.getId()+System.currentTimeMillis());
        }finally {
            locks.unlock();
        }
    }
}
