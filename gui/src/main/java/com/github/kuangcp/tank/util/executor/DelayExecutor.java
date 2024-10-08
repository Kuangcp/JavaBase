package com.github.kuangcp.tank.util.executor;

import com.github.kuangcp.tank.domain.event.DelayEvent;
import com.github.kuangcp.tank.util.ExecutePool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-17 01:35
 */
public class DelayExecutor {

    private static final BlockingQueue<DelayEvent> queue = new DelayQueue<>();

    /**
     * 循环事件线程池
     */
    private static final ExecutorService delayEventPool = ExecutePool.buildFixedPool("delayEvent", 4);

    public static void init() {
        delayEventPool.execute(() -> CommonEventExecutor.delayEventSpin(queue));
    }

    public static void addEvent(DelayEvent event) {
        queue.add(event);
    }
}
