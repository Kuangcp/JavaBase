package com.github.kuangcp.sync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-04-08 15:07
 */
@Slf4j
public class ClosableCacheTest {

    @Test
    public void testClose() throws Exception {
        ClosableCache cache = new ClosableCache();
        int num = 50;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(90);
        for (int i = 1; i <= num; i++) {
            fixedThreadPool.execute(() -> {
                for (; ; ) {
                    if (cache.flag) {
                        return;
                    }
                    int result = (int) ((Math.random() * 100) / 10 + 1);
//                    log.info("{}", result);
                    if (result < 2) {
                        log.info("准备关闭");
                        cache.close(fixedThreadPool);
                    } else {
                        cache.doOther();
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            });
        }
//        fixedThreadPool.shutdown();
        Thread.currentThread().join();
    }
}
