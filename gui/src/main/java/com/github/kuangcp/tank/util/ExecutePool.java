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

    public static final AtomicLong totalCounter = new AtomicLong();

    public static ExecutorService buildFixedPool(String prefix, int coreSize) {
        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> new Thread(runnable, prefix + "-" + totalCounter.addAndGet(1)));
    }
}
