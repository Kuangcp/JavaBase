package com.github.kuangcp.latch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author https://github.com/kuangcp on 2021-09-04 23:02
 */
@Slf4j
public class CountDownLatchTest {

    @Test
    public void testFirstUse() throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(3);
        final CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            pool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.info("fin: {}", finalI);
                    latch.countDown();
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }

        latch.await(3, TimeUnit.SECONDS);
    }

    @Test
    public void testSemaphore() throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 30; i++) {
            pool.execute(() -> {
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    semaphore.release();
                }
                log.info("run");
            });
        }

        Thread.currentThread().join();
    }
}
