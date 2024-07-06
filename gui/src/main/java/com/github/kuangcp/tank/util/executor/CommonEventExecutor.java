package com.github.kuangcp.tank.util.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-17 01:06
 */
@Slf4j
public class CommonEventExecutor {

    /**
     * 事件循环任务
     */
    public static void loopEventSpin(String type, BlockingQueue<AbstractLoopEvent> queue) {
        while (true) {
            try {
                final AbstractLoopEvent event = queue.take();
                final long delay = event.getDelay(TimeUnit.MILLISECONDS);
                // 事件延迟调度警告，且过滤掉初次运行任务
                if (delay < -200 && delay > -1000_000) {
                    log.info("[{}] delay {}ms", type, delay);
                }
                final long start = System.nanoTime();
                event.run();
                final long taskWaste = System.nanoTime() - start;
                if (taskWaste > event.fixedDelayTime * 1000_000) {
                    log.warn("task run out of fixed delay. waste:{}ns, fixed:{}ms", taskWaste, event.fixedDelayTime);
                }
                if (event.isContinue() && event.addFixedDelay()) {
                    queue.add(event);
                }
            } catch (InterruptedException e) {
                log.error("[{}]invoke loop event error", type, e);
                break;
            }
        }
    }

    /**
     * 单次延迟任务
     */
    public static void delayEventSpin(BlockingQueue<AbstractDelayEvent> queue) {
        while (true) {
            try {
                final AbstractDelayEvent event = queue.take();
                event.run();
            } catch (InterruptedException e) {
                log.error("invoke delay event error", e);
                break;
            }
        }
    }
}
