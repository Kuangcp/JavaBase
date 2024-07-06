package com.github.kuangcp.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-04 23:02
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

}
