package com.github.kuangcp.block;

import com.github.kuangcp.old.Food;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by https://github.com/kuangcp on 17-8-14  下午9:16
 * 解决死锁:
 *      如果在取得第二个锁的时候失败了，就将第一个锁释放掉，从头开始等待
 *      但是还是会有问题，并不是书上说的那么美好，如果遇到锁就释放，然后等待，两个线程都是这样的情况，
 *      然后就不停的释放，等待，加锁，死锁在了各自的第一个方法上，控制台就不停的刷准备方法的内容
 */
public class HandleDeadLock {
    public static void main(String []s){

        DeadLockHandle one = new  DeadLockHandle("one");
        DeadLockHandle other = new  DeadLockHandle("other");
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
class DeadLockHandle{
    private final String id;
    private final Lock locks = new ReentrantLock();

    public DeadLockHandle(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void preparRun(Food food, DeadLockHandle lock){
        boolean required = false;
        boolean done = false;

        while (!done){
            int wait = (int)(Math.random() * 10);
            try{
//                Thread.sleep(2000);
                required = locks.tryLock(wait, TimeUnit.MILLISECONDS); // 尝试与锁定，超时时长随机
                if (required){
                    System.out.println("准备 currentId: " + id + " resource: " + food.getName() + " prepar other: " + lock.getId()+System.currentTimeMillis());
                    done = lock.confirmRun(food, this); // 检查返回值
                }else{
                    Thread.sleep(wait);
                }
            }catch (InterruptedException e){
            }finally {
                if (done) locks.unlock(); // 如果done为假，释放锁并等待
            }
            if (!done){
                try {
                    Thread.sleep(wait);
                }catch (InterruptedException e){
                }
            }
        }
    }
    public boolean confirmRun(Food food,  DeadLockHandle lock){
        boolean required = false;
        try{
            int wait = (int)(Math.random() * 10);
            required = locks.tryLock(wait, TimeUnit.MILLISECONDS);
            if (required){
                System.out.println("确认 currentId: " + id + " resource: " + food.getName() + "confirm other: " + lock.getId()+System.currentTimeMillis());
                return true;
            }
        }catch (InterruptedException e){
        }finally {
            if (required){
                locks.unlock();
            }
        }
        return false;
    }

}