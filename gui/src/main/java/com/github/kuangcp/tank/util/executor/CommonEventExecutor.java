package com.github.kuangcp.tank.util.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * @author https://github.com/kuangcp on 2021-09-17 01:06
 */
@Slf4j
public class CommonEventExecutor {

    public static void simpleMainLoop(BlockingQueue<AbstractLoopEvent> queue) {
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
}
