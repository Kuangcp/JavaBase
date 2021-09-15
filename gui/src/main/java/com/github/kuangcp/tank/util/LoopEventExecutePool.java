package com.github.kuangcp.tank.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * @author https://github.com/kuangcp on 2021-09-16 01:20
 */
@Slf4j
public class LoopEventExecutePool {

    private static final BlockingQueue<AbstractLoopEvent> queue = new DelayQueue<>();

    public static void init() {
        final Runnable mainLoop = () -> {
            while (true) {
                try {
                    final AbstractLoopEvent event = queue.take();
//                    log.info("event {}", event);
                    event.run();
                    if (event.isContinue() && event.addFixedDelay()) {
                        queue.add(event);
                    }
                } catch (InterruptedException e) {
                    log.error("", e);
                    break;
                }
            }
        };

        for (int i = 0; i < ExecutePool.EVENT_POOL_SIZE; i++) {
            ExecutePool.loopEventPool.execute(mainLoop);
        }
    }

    public static void addLoopEvent(AbstractLoopEvent loopEvent) {
//        log.info("add: loopEvent={}", loopEvent);
        queue.add(loopEvent);
    }

}

