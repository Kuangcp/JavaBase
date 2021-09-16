package com.github.kuangcp.tank.util.executor;

import com.github.kuangcp.tank.util.ExecutePool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author https://github.com/kuangcp on 2021-09-16 01:20
 */
@Slf4j
public class LoopEventExecutor {

    static final BlockingQueue<AbstractLoopEvent> queue = new DelayQueue<>();

    private static final int EVENT_POOL_SIZE = 6;

    /**
     * 循环事件线程池
     */
    private static final ExecutorService loopEventPool = ExecutePool.buildFixedPool("loopEvent", EVENT_POOL_SIZE);

    public static void init() {
        for (int i = 0; i < EVENT_POOL_SIZE; i++) {
            loopEventPool.execute(() -> CommonEventExecutor.loopEventSpin(queue));
        }
    }

    public static void addLoopEvent(AbstractLoopEvent loopEvent) {
//        log.info("add: loopEvent={}", loopEvent);
        queue.add(loopEvent);
    }

}

