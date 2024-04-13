package com.github.kuangcp.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-04-13 12:01
 */
@Slf4j
public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final DefaultUncaughtExceptionHandler INSTANCE = new DefaultUncaughtExceptionHandler();

    private static final LongAdder uncaughtExceptionCount = new LongAdder();

    private DefaultUncaughtExceptionHandler() {
    }

    public static DefaultUncaughtExceptionHandler getInstance() {
        return INSTANCE;
    }

    public static long getUncaughtExceptionCount() {
        return uncaughtExceptionCount.longValue();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        uncaughtExceptionCount.add(1);
        log.error("Caught an exception in {}.", t, e);
    }
}
