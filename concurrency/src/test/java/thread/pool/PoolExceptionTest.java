package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.pool.ExecutorsThreadPool.*;

/**
 * @author Kuangcp
 * 2024-04-09 20:06
 */
@Slf4j
public class PoolExceptionTest {


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
}
