package com.github.kuangcp.queue.use.blocking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-06-07 15:02
 */
@Slf4j
public class ReaderWriterTest {


    @Test
    public void testChannel() throws Exception {
        ArrayBlockingQueue<List<String>> queue = new ArrayBlockingQueue<>(500);
        QueueChannel<List<String>> channel = new QueueChannel<>(queue);

        CountDownLatch latch = startWriter(channel);
        startReader(channel);
        latch.await();
    }

    private void startReader(QueueChannel<List<String>> channel) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            channel.getQueue().add(Arrays.asList("33-" + i, "vv"));
        }
    }

    private CountDownLatch startWriter(QueueChannel<List<String>> channel) {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        pool.execute(() -> {
            try {
                log.info("start");
                while (!channel.getStop().get()) {
                    List<String> task = channel.getQueue().poll(1, TimeUnit.SECONDS);
                    if (Objects.isNull(task)) {
                        continue;
                    }
                    log.info("task={}", task);
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                latch.countDown();
            }
        });
        return latch;
    }


}
