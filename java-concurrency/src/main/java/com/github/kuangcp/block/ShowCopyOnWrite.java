package com.github.kuangcp.block;

import com.github.kuangcp.old.BuildFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by https://github.com/kuangcp on 17-8-15  下午8:55
 * 展示写时复制的特性，使用上锁存器 CountDownLatch 严格控制事件的顺序
 *      虽然线程的启动1在前， 但是锁存器控制了 2 在 1 之前，且很清楚的表明了写时复制的特性，
 *      第一个线程添加了一个元素，然后就得到了复制数组，就被挂起了，然后第二个线程又添加一个，运行结束后将1唤醒，运行结束
 */
public class ShowCopyOnWrite {

    public static void main(String []a){
        final CountDownLatch firstLatch = new CountDownLatch(1);
        final CountDownLatch secondLatch = new CountDownLatch(1);
        // 使用不可变对象
        final BuildFactory.Builder builder = new BuildFactory.Builder();

        CopyOnWriteArrayList<BuildFactory> elements = new CopyOnWriteArrayList<>();
        elements.add(builder.name("myth").addr("local").build());
        elements.add(builder.name("roll").addr("remote").build());
        ReentrantLock lock = new ReentrantLock();

        ElementList list = new ElementList(elements, lock, "list1 > ");
        ElementList list2 = new ElementList(elements, lock, "list2 > ");

        Thread thread1 = new Thread(() -> {
            System.out.println("进入 线程 1");
            elements.add(builder.name("dyn 1").addr("89").build());
            list.prep();
            // 将计数器减一，也就是减成了0
            firstLatch.countDown();
//                System.out.println(firstLatch.toString());
            try{
                secondLatch.await();
            }catch (InterruptedException e){
                System.out.println("first inter error");
            }
            list.listElement("first ");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("进入 线程 2");
            try{
                Thread.sleep(2000);
                firstLatch.await();
                elements.add(builder.name("dyn 2").addr("00").build());
                list2.prep();
                // 唤醒 线程1
                secondLatch.countDown();
            }catch (InterruptedException e){
                System.out.println("second inter error");
            }
            list2.listElement("second");
        });

        thread1.start();
        thread2.start();
    }
}
