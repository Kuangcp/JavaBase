package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static thread.pool.UseThreadPool.*;

/**
 *
 * @author Kuangcp
 * 2024-04-01 11:33
 */
@Slf4j
public class UseThreadPoolTest {
    @Test
    public void testCompareExecuteAndSubmit() throws Exception {
        pool.execute(() -> {
            log.info("pool execute");
        });
        scheduler.execute(() -> {
            log.info("scheduler execute");
        });


        Future<String> poolSubmit = pool.submit(() -> {
            log.info("pool submit");
            return "pool";
        });
        Future<String> schedulerSubmit = scheduler.submit(() -> {
            log.info("scheduler submit");
            return "scheduler";
        });
        log.info("{}", poolSubmit.get());
        log.info("{}", schedulerSubmit.get());

        Thread.currentThread().join();
    }

    /**
     * @see ScheduledThreadPoolExecutor.ScheduledFutureTask#run()
     * @see ScheduledThreadPoolExecutor#schedule(Runnable, long, TimeUnit) execute 实际调用的还是 schedule
     */
    @Test
    public void testExecuteException() throws Exception {
        pool.execute(() -> {
            log.info("pool execute");
            throw new RuntimeException("pool execute error");
        });
        scheduler.execute(() -> {
            log.info("scheduler execute");
            // 注意 这个异常不会被感知（没有日志和System.err输出），虽然是execute 但异常会被捕获并封装返回在 FutureTask
            throw new RuntimeException("scheduler execute error");
        });

        Thread.currentThread().join();
    }

    @Test
    public void testExecuteExceptionWithDefaultHandler() throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("", e);
        });
        pool.execute(() -> {
            log.info("pool execute");
            throw new RuntimeException("pool execute error");
        });

        scheduler.execute(() -> {
            log.info("scheduler execute");
            // 注意 这个异常仍然不会被感知，因为默认handler只能处理未被捕获的异常，这里是捕获了且没有后续动作
            throw new RuntimeException("scheduler execute error");
        });

        Thread.currentThread().join();
    }

    @Test
    public void testExecuteExceptionForScheduler() throws Exception {
        customScheduler.execute(() -> {
            log.info("scheduler execute");
            // 此时抛出的异常可被感知了
            throw new RuntimeException("scheduler execute error");
        });

        Thread.currentThread().join(3000);
    }

    @Test
    public void testCallableScheduler() throws Exception {
        Future<String> submitError = customScheduler.submit(() -> {
            if (2 > 1) {
                throw new RuntimeException("submit error");
            }
            return "OK";
        });
        ScheduledFuture<String> scheduleError = customScheduler.schedule(() -> {
            if (2 > 1) {
                throw new RuntimeException("schedule error");
            }
            return "OK";
        }, 1, TimeUnit.SECONDS);


        log.info("={}", scheduleError.get());
        log.info("={}", submitError.get());
        Thread.currentThread().join(3000);
    }

    @Test
    public void testLoopScheduler() throws Exception {
        AtomicInteger counter = new AtomicInteger();
        ScheduledFuture<?> future = customScheduler.scheduleAtFixedRate(() -> {
            System.out.println("xx");
            int cnt = counter.incrementAndGet();
            if (cnt > 5) {
                throw new RuntimeException("xx");
            }
        }, 1, 1, TimeUnit.SECONDS);

        log.info("FUTURE {}", future.get());

        Thread.currentThread().join(3000);
    }
}
