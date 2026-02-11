package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    @Test
    public void testBreak() throws Exception {
        CusSchedulePool sche = new CusSchedulePool(1, "she");
        AtomicInteger cnt = new AtomicInteger();
        // 达到特定条件时抛出异常，可取消周期任务
        sche.scheduleAtFixedRate(() -> {
            int i = cnt.incrementAndGet();
            if (i > 10) {
                throw new RuntimeException("break fixed scheduler");
            }
            System.out.println("run " + i);
        }, 3, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    @Test
    public void testBreakAndDown() throws Exception {
        CusSchedulePool sche = new CusSchedulePool(1, "she");
        AtomicInteger cnt = new AtomicInteger();
        // 需要在声明完之前就引用，所以需要借助AtomicReference来 逆时序传递 对象
        AtomicReference<ScheduledFuture<?>> monitorFutureRef = new AtomicReference<>();
        ScheduledFuture<?> monitorFuture = sche.scheduleAtFixedRate(() -> {
            int i = cnt.incrementAndGet();
            if (i > 5) {
                // 取消定时任务
                ScheduledFuture<?> future = monitorFutureRef.get();
                if (future != null) {
                    future.cancel(false);
                }
                // 关闭线程池
                sche.shutdown();
            }
            System.out.println("run " + i);
        }, 2, 1, TimeUnit.SECONDS);
        monitorFutureRef.set(monitorFuture);

        Thread.currentThread().join();
    }
}
