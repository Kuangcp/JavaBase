package com.github.kuangcp.queue.use.blocking;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kuangcp
 * 2024-06-07 15:09
 */
@Data
public class QueueChannel<E> {

    private BlockingQueue<E> queue;
    private AtomicBoolean stop = new AtomicBoolean(false);

    public QueueChannel(BlockingQueue<E> queue) {
        this.queue = queue;
    }
}
