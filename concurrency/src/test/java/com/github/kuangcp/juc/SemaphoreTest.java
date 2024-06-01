package com.github.kuangcp.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-06-01 17:39
 */
@Slf4j
public class SemaphoreTest {

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
