package com.github.kuangcp.tank.util.executor;

import com.github.kuangcp.tank.util.ExecutePool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-17 00:58
 */
@Slf4j
public class MonitorExecutor {

    public static final Info info = new Info();
    private static final BlockingQueue<AbstractLoopEvent> queue = new DelayQueue<>();
    private static final ExecutorService monitorEventPool = ExecutePool.buildFixedPool("monitorEvent", 1);

    public static void init() {
        // 1. 初始化其他 执行器
        DelayExecutor.init();
        LoopEventExecutor.init();

        // 2. 注册管理任务
        registerEventMonitor();

        monitorEventPool.execute(() -> CommonEventExecutor.loopEventSpin(MonitorExecutor.class.getSimpleName(), queue));
    }

    public static class Info {
        int loopEventCount;
        int monitorEventCount;

        @Override
        public String toString() {
            return "loop:" + loopEventCount + " monitor:" + monitorEventCount;
        }
    }

    private static void registerEventMonitor() {
        final AbstractLoopEvent loopEventMonitor = new AbstractLoopEvent() {
            @Override
            public void run() {
                info.loopEventCount = LoopEventExecutor.queue.size();
                info.monitorEventCount = queue.size();
            }
        };
        loopEventMonitor.setFixedDelayTime(5_000);
        queue.add(loopEventMonitor);
    }

    public static void addLoopEvent(AbstractLoopEvent loopEvent) {
        queue.add(loopEvent);
    }
}
