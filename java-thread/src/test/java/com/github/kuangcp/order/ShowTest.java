package com.github.kuangcp.order;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:54
 * 多线程有序的输出结果  参考博客 http://www.jb51.net/article/108762.htm
 *  在Junit中, test方法来测试多线程, 因为一个Test注解的方法看做main方法的时候, 没有阻塞的,所以直接退出了
 *  里面写的开启的线程都是隶属于他的子线程, 所以也一起跟着关闭了.
 *  思路:
 *
 * @author kuangcp
 */
public class ShowTest {

    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[]s) {
        ShowTest test = new ShowTest();

//        test.testOrder();
        test.testOrder2();
    }
    @Test
    public void testOrder() {
        Thread thread1 = new Thread(new Show("A", 0, count));
        thread1.start();
//        thread1.join();
        Thread thread2 = new Thread(new Show("B", 1, count));
        thread2.start();
        Thread thread3 = new Thread(new Show("C", 2, count));
        thread3.start();
        Thread thread4 = new Thread(new Show("D", 3, count));
        thread4.start();
        Thread thread5 = new Thread(new Show("E", 4, count));
        thread5.start();
//        Scanner sc = new Scanner(System.in);
//        sc.nextLine();
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    static Holder holder = new Holder();
    @Test
    public void testOrder2(){
        Thread thread1 = new Thread(new ShowWithVolatile("A", 0, holder));
        thread1.start();
        Thread thread2 = new Thread(new ShowWithVolatile("B", 1, holder));
        thread2.start();
        Thread thread3 = new Thread(new ShowWithVolatile("C", 2, holder));
        thread3.start();
        Thread thread4 = new Thread(new ShowWithVolatile("D", 3, holder));
        thread4.start();
        Thread thread5 = new Thread(new ShowWithVolatile("E", 4, holder));
        thread5.start();


    }
}