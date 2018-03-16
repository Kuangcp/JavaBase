package com.github.kuangcp.block;

import com.github.kuangcp.old.Food;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午8:58
 * 尝试 解决死锁
 *      当遇到锁进行休眠，但是没有释放已经占用的锁，所以还是很大几率死锁
 */
public class TryHandleDeadLock {
    public static void main(String []s){

        DeadLocks one = new  DeadLocks("one");
        DeadLocks other = new  DeadLocks("other");
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

class DeadLocks {
    private final String id;
    private final Lock locks = new ReentrantLock();

    public DeadLocks(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void preparRun(Food food, DeadLocks lock){
        boolean required = false;
        while (!required){
            try{
                int wait = (int)(Math.random() * 10);
                required = locks.tryLock(wait, TimeUnit.MILLISECONDS); // 尝试与锁定，超时时长随机
                if (required){
                    System.out.println("准备 currentId: " + id + " resource: " + food.getName() + " prepar other: " + lock.getId()+System.currentTimeMillis());
                    lock.confirmRun(food, this); // 给主线程确认
                }else{
                    Thread.sleep(wait);
                }

            }catch (InterruptedException e){

            }finally {
                if (required) locks.unlock(); // 如果锁定就解锁
            }
        }
    }
    public void confirmRun(Food food,  DeadLocks lock){
        locks.lock(); // 尝试锁住其他线程 正是这里可能出现死锁，因为这个其他线程已经加锁这里就死锁了
        try {
            System.out.println("确认 currentId: " + id + " resource: " + food.getName() + "confirm other: " + lock.getId()+System.currentTimeMillis());
        }finally {
            locks.unlock();
        }
    }
}