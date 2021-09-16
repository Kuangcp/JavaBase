package com.github.kuangcp.tank.util.executor;

import com.github.kuangcp.tank.util.ExecutePool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author https://github.com/kuangcp on 2021-09-17 00:58
 */
@Slf4j
public class MonitorExecutor {

    private static final BlockingQueue<AbstractLoopEvent> queue = new DelayQueue<>();
    /**
     * 循环事件线程池
     */
    private static final ExecutorService monitorEventPool = ExecutePool.buildFixedPool("monitorEvent", 1);

    public static void init() {
        registerEventMonitor();

        monitorEventPool.execute(() -> CommonEventExecutor.simpleMainLoop(queue));
    }

    private static void registerEventMonitor() {
        final AbstractLoopEvent loopEventMonitor = new AbstractLoopEvent() {
            @Override
            public void run() {
                log.info("loopEvent:{} monitor:{}", LoopEventExecutor.queue.size(), queue.size());
            }
        };
        loopEventMonitor.setFixedDelayTime(5_000);
        queue.add(loopEventMonitor);
    }
}
