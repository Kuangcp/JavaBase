package com.github.kuangcp.queue.disruptor.first;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-09-24 16:28
 */
@Slf4j
public class OrderEventSlowHandler extends OrderEventHandler {

    public OrderEventSlowHandler() {
    }

    public OrderEventSlowHandler(String id) {
        super(id);
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        log.info("{} event: {}, sequence: {}, endOfBatch: {}", getId(), event, sequence, endOfBatch);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEvent(OrderEvent event) {
        log.info("{} event: {}", getId(), event);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
