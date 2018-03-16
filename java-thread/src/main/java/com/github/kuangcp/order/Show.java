package com.github.kuangcp.order;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:53
 * 利用原子递增控制线程准入顺序。
 * @author kuangcp
 */
public class Show implements Runnable{
    private String target;
    private int order;
    private AtomicInteger count; //利用原子递增控制线程准入顺序。

    public Show(String target, int order, AtomicInteger count) {
        this.target = target;
        this.order = order;
        this.count = count;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count.get() % 5 == order) {
                System.out.print(target);
                count.incrementAndGet();
            }

        }
    }
}
