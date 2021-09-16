package com.github.kuangcp.tank.util.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

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
                event.run();
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
