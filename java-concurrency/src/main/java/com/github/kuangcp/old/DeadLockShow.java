package com.github.kuangcp.old;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午9:40
 * 线程死锁的案例类 ：
 * 原理是
 *      线程1 得到资源A的锁，然后等主线程确认后再获取B的锁
 *      线程2得到B的锁，然后等主线程确认后去获取A的锁，双方都在等对方释放锁，然后就陷入了死锁,
 *      但是： 因为线程在JVM的运行是随机性的，所以可能出现线程1 占有了两个资源，运行结束了，
 *          线程2才开始运行，所以就没有死锁
 *      所以在准备那个方法里加上睡眠时间，延长运行时间就加大了死锁的概率
 * 消除死锁：
 *      让所有的线程以相同的顺序去获取线程锁，这个案例是AB BA的顺序获取，就很有可能出现死锁
 *      对于完全同步对象而言，防止这种死锁出现是因为这样的代码 破坏了状态一致性规则，当有调用准备方法时
 *          接收的方法会在方法内调用另外一个对象，这个调用时的状态是不一致的
 *
 */
public class DeadLockShow{
    public static void main(String []s){
        mainTest();
    }

    public static void mainTest(){
        DeadLock one = new DeadLock("one");
        DeadLock other = new DeadLock("other");
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



