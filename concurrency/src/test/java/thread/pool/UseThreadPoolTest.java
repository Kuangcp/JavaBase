package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 *
 * @author Kuangcp
 * 2024-04-01 11:33
 */
@Slf4j
public class UseThreadPoolTest {
    ExecutorService pool = Executors.newFixedThreadPool(1);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledExecutorService customScheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
        /**
         * 通过 execute submit schedule 提交的 Runnable 任务： 手动传递上下文, 捕获异常
         */
        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            if (command == null) {
                throw new RuntimeException("schedule command NPE");
            }
            return super.schedule(() -> {
                try {
                    command.run();
                } catch (Throwable e) {
                    log.error("", e);
                }
            }, delay, unit);
        }

        /**
         * 通过 execute submit schedule 提交的 Callable 任务 手动传递上下文, 捕获异常
         * 由于 Callable 会返回Future，此时try catch 要throw 异常才能不影响原逻辑，好处是不用担心业务代码漏 get 从而吞异常，缺点是异常栈会打两次日志
         */
        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            if (callable == null) {
                throw new RuntimeException("schedule callable NPE");
            }
            return super.schedule(() -> {
                try {
                    return callable.call();
                } catch (Throwable e) {
                    log.error("", e);
                    throw e;
                }
            }, delay, unit);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            // 获取不到抛出的异常
            super.afterExecute(r, t);
        }
    };

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

        Thread.currentThread().join(5000);
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
}
