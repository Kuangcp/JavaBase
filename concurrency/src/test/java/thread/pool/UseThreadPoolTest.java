package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static thread.pool.UseThreadPool.*;

/**
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

        Thread.currentThread().join(2000);
    }
}
