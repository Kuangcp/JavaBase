package com.github.kuangcp.tank.util.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * TODO 消费任务时，延迟情况告警
 *
 * @author https://github.com/kuangcp on 2021-09-17 01:06
 */
@Slf4j
public class CommonEventExecutor {

    public static void loopEventSpin(BlockingQueue<AbstractLoopEvent> queue) {
        while (true) {
            try {
                final AbstractLoopEvent event = queue.take();
                final long delay = event.getDelay(TimeUnit.MILLISECONDS);
                if (delay < -200) {
                    log.info("delay {}ms", delay);
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
                log.error("", e);
                break;
            }
        }
    }

    public static void delayEventSpin(BlockingQueue<AbstractDelayEvent> queue) {
        while (true) {
            try {
                final AbstractDelayEvent event = queue.take();
                event.run();
            } catch (InterruptedException e) {
                log.error("", e);
                break;
            }
        }
    }
}
