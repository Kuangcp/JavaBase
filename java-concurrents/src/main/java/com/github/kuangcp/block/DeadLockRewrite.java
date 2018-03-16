package com.github.kuangcp.block;

import com.github.kuangcp.old.Food;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午10:23
 * 使用并发包重写死锁案例：
 *      但是使用的时候，似乎lock方法会一直尝试获得锁，睡眠时间决定了死锁状况的长短（这应该不算死锁，只是等待）准备到确认的延时恰好是睡眠时间
 *      根据 API 知道这个lock如果发现资源被别的线程锁住，他就会休眠，等待锁的释放，所以这里的写法是不会有死锁的，可能7和8的区别？书上用的是7，说的是死锁
 */
public class DeadLockRewrite {
    public static void main(String []s){

        DeadLockre one = new  DeadLockre("one");
        DeadLockre other = new  DeadLockre("other");
        Food fooda = new Food("a");
        Food foodb = new Food("b");

        new Thread(new Runnable() {
            @Override
            public void run() {
                one.preparRun(fooda, other);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                other.preparRun(foodb, one);
            }
        }).start();
    }
}

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
