package com.github.kuangcp.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-04-08 15:05
 */
@Slf4j
public class ClosableCache {

    volatile ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    volatile boolean flag = false;

    ClosableCache() {
        map.put("12", "22");
    }


    /**
     * 进行操作, 原则上是 close之后, 就不允许执行这个方法了, 那么多线程共享的map变量就需要修饰一下了
     */
    void doOther() {
        if (flag) {
            return;
        }
        // 这里如果使用this或者map效果一样, 那么就是对对象加锁,多线程要依次等待锁释放, 所以执行次数和循环调用次数不一致
        synchronized (this) {
            if (flag) {
                return;
            }
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum *= i;
                sum -= i;
            }
            log.info(System.currentTimeMillis() + " 计算 " + sum + " | " + map.size());
        }
    }

    void close(ExecutorService fixedThreadPool) {
        // 如果没有在锁代码块中再次判断 90进程 :直接对 this 加锁 大约是 1-10个 和对map加锁基本一致

        // 如果使用volatile关键字修饰标识变量, 也做不到保证一次, 因为只是保证了并发读, 并发写不能保证
        // 使用锁加上双重判断就不会有问题
        synchronized (this) {
            if (flag) {
                return;
            }
            map.remove("12");
            flag = true;
            log.info("关闭");
            try {
                boolean close = fixedThreadPool.awaitTermination(2, TimeUnit.SECONDS);
                log.info("关闭线程池 {}", close);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }
}
