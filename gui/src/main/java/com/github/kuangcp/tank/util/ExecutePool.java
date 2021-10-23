package com.github.kuangcp.tank.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author https://github.com/kuangcp on 2021-09-11 17:32
 */
public class ExecutePool {

    // 敌人全部的子弹线程实现并发，目前选用事件延迟队列。原有方案：
    // 1. 线程池
//    public static final ExecutorService shotPool = ExecutePool.buildFixedPool("enemyShot", 5);

    // 2. ForkJoin
//    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(65);

    // 3. 协程池 http://docs.paralleluniverse.co/quasar/
    // 在当前场景下，没有优势，底层一样使用 类似JDK的ForkJoinPool实现， 对比50敌人150子弹情况下一样启动了63个真实线程，和上述没区别
//    public static FiberForkJoinScheduler shotScheduler = new FiberForkJoinScheduler("enemyShot", 20, null, false);

    public static final AtomicLong totalCounter = new AtomicLong();

    public static final ExecutorService exclusiveLoopPool = ExecutePool.buildFixedPool("exclusiveLoopPool", 2);

    /**
     * @param prefix   eg: shot
     * @param coreSize 核心线程数
     */
    public static ExecutorService buildFixedPool(String prefix, int coreSize) {
        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> new Thread(runnable, prefix + "-" + totalCounter.addAndGet(1)));
    }
}
