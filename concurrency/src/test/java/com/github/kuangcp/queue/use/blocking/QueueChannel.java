package com.github.kuangcp.queue.use.blocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kuangcp
 * 2024-06-07 15:09
 */
public class QueueChannel<E> {

    private final BlockingQueue<E> queue;
    private final AtomicBoolean stop = new AtomicBoolean(false);

    public QueueChannel(BlockingQueue<E> queue) {
        this.queue = queue;
    }

    public void stop() {
        this.stop.set(true);
    }

    public boolean isRunning() {
        return !this.stop.get();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    public void put(E e) throws InterruptedException {
        queue.put(e);
    }

    public boolean add(E e) {
        return queue.add(e);
    }
}
