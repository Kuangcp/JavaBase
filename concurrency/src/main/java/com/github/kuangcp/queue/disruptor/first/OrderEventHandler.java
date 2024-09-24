package com.github.kuangcp.queue.disruptor.first;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kuangcp
 * 2024-09-24 16:08
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    private final String id;

    public OrderEventHandler() {
        this.id = "";
    }

    public OrderEventHandler(String id) {
        this.id = id;
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        log.info("{} event: {}, sequence: {}, endOfBatch: {}", id, event, sequence, endOfBatch);
    }

    @Override
    public void onEvent(OrderEvent event) {
        log.info("{} event: {}", id, event);
    }
}
