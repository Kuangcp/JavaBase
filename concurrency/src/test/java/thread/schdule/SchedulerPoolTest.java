package thread.schdule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static thread.pool.UseThreadPool.customScheduler;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-30 15:17
 */
@Slf4j
public class SchedulerPoolTest {

    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }

    @Test
    public void testSimple() throws Exception {
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                printException(r, t);
            }

            /**
             * 手动传递上下文
             */
            @Override
            public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
                if (command == null) {
                    throw new RuntimeException("schedule command NPE");
                }
                String tid = MDC.get("tid");
                return super.schedule(() -> {
                    if (StringUtils.isNoneBlank(tid)) {
                        MDC.put("tid", tid);
                    }
                    command.run();
                }, delay, unit);
            }

            @Override
            protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
                return super.decorateTask(runnable, task);
            }
        };

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            MDC.put("tid", "U" + i);
            TimeUnit.SECONDS.sleep(1);
            log.info("SUBMIT {}", finalI);
            pool.schedule(() -> {
                log.info("RUN {} {}", finalI, MDC.get("tid"));
            }, 3, TimeUnit.SECONDS);
        }

        Thread.currentThread().join();
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
        ScheduledFuture<?> future = customScheduler.scheduleWithFixedDelay(() -> {
            System.out.println("xx");
            int cnt = counter.incrementAndGet();
            if (cnt > 5) {
                throw new RuntimeException("xx");
            }
        }, 1, 1, TimeUnit.SECONDS);

        // get 时会抛出任务中的异常
        log.info("FUTURE {}", future.get());

        Thread.currentThread().join(3000);
    }

    @Test
    public void testCancelSchedule() throws Exception {
        ScheduledFuture<?> future = customScheduler.scheduleWithFixedDelay(() -> {
            log.info("RUN");
        }, 1, 1, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(5);
        future.cancel(false);
        Thread.currentThread().join();
    }

    /**
     * @see ScheduledThreadPoolExecutor.ScheduledFutureTask#run() TODO 找问题
     */
    @Test
    public void testFixed() throws Exception {
        // 3s 定时
        customScheduler.scheduleAtFixedRate(() -> {
            log.info("RUN start");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {

            }
            log.info("RUN stop");
        }, 2, 1, TimeUnit.SECONDS);

        // 4s 定时 会累积
        // TODO 但是核心线程数是1时，这个任务的定时延迟固定在  24,75,229,687 ...
        customScheduler.scheduleWithFixedDelay(() -> {
            log.info("RUN2 start");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {

            }
            log.info("RUN2 stop");
        }, 2, 1, TimeUnit.SECONDS);

        ScheduledExecutorService watch = Executors.newScheduledThreadPool(1);
        watch.scheduleAtFixedRate(() -> {
            BlockingQueue<Runnable> queue = ((ScheduledThreadPoolExecutor) customScheduler).getQueue();
            StringBuilder res = new StringBuilder();
            for (Runnable r : queue) {
                res.append(r.toString()).append(",");
            }
//            String str = queue.stream().map(Object::toString).collect(Collectors.joining(","));
            log.info("str={}", res);
        }, 1, 1, TimeUnit.SECONDS);


        Thread.currentThread().join();
    }

    @Test
    public void testSchedulerGetResult() throws Exception {
        long start = System.currentTimeMillis();
        ScheduledFuture<?> future = customScheduler.scheduleWithFixedDelay(() -> {
            log.info("RUN2");

            if (System.currentTimeMillis() - start > 5000) {
                throw new RuntimeException("abort");
            }

        }, 2, 1, TimeUnit.SECONDS);
        try {
            Object o = future.get();
        } catch (Exception e) {
            log.error("", e);
            future.cancel(false);
        }

        Thread.currentThread().join(10000);
    }

    @Test
    public void testScheduleDrop() throws Exception {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            customScheduler.scheduleAtFixedRate(() -> {
                log.info("RUN {}", finalI);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {

                }
            }, 2, 1, TimeUnit.SECONDS);
        }
        Thread.currentThread().join(30000);
    }

    @Test
    public void testLimitRunBatchSleep() throws Exception {
        Semaphore semaphore = new Semaphore(4);

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            semaphore.acquire();
            // 模拟业务触发逻辑
            log.info("Start {}", finalI);

            AtomicInteger count = new AtomicInteger();
            pool.execute(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    log.info("Check {}", finalI);
                    // 模拟业务结束逻辑
                    int now = count.incrementAndGet();
                    if (now > 5) {
                        semaphore.release();
                        log.info("End {}", finalI);
                        break;
                    }
                }
            });
        }
        Thread.currentThread().join(30000);
    }

    @Test
    public void testLimitRunBatch() throws Exception {
        Semaphore semaphore = new Semaphore(4);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            semaphore.acquire();
            // 模拟业务触发逻辑
            log.info("Start {}", finalI);

            AtomicInteger count = new AtomicInteger();
            customScheduler.scheduleAtFixedRate(() -> {
                log.info("Check {}", finalI);
                // 模拟业务结束逻辑
                int now = count.incrementAndGet();
                if (now > 5) {
                    semaphore.release();
                    log.info("End {}", finalI);
                    // 本来尝试用提交任务的 Future 对象来cancel任务，但是该逻辑必然无法实现
                    throw new RuntimeException("End");
                }
            }, 2, 1, TimeUnit.SECONDS);
        }
        Thread.currentThread().join(30000);
    }
}
