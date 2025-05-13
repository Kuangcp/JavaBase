package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2025-05-13 14:44
 */
@Slf4j
public class CusBenchPoolTest {

    @Test
    public void testShow() throws Exception {
        CusBenchPool pool = new CusBenchPool(10, 20,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000),
                new BasicThreadFactory.Builder().namingPattern("limit-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        CusSchedulePool sche = new CusSchedulePool(1);
        sche.scheduleAtFixedRate(() -> {
            log.info("Run {}", pool.statisticsTask().format());
        }, 2, 3, TimeUnit.SECONDS);

        int total = 1000;
        for (int i = 0; i < total; i++) {
            pool.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500 + ThreadLocalRandom.current().nextInt(600));
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }


        Thread.currentThread().join();
    }
}
