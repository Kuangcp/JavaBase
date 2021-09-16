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
    private static final ExecutorService monitorEventPool = ExecutePool.buildFixedPool("monitorEvent", 1);

    public static void init() {
        // 1. 初始化其他 执行器
        DelayExecutor.init();
        LoopEventExecutor.init();

        // 2. 注册管理任务
        registerEventMonitor();

        monitorEventPool.execute(() -> CommonEventExecutor.loopEventSpin(queue));
    }

    private static void registerEventMonitor() {
        // TODO 更新到主背景图上
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
