package thread.pool;


import com.hellokaton.blade.Blade;
import com.hellokaton.blade.mvc.RouteContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import web.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kuangcp
 * 2024-05-15 10:06
 */
@Slf4j
public class RecommendUsePoolTest {

    /**
     * 最终会丢弃10个任务（不知道哪10个），5个任务分配给线程 5个在队列里
     *
     * @see RecommendUsePoolTest#testTaskPool() 优化版
     */
    @Test
    public void testDiscard() throws Exception {
        for (int i = 0; i < 20; i++) {
            int finalI = i + 1;
            log.info("e={}", RecommendUsePool.discardPool);
            RecommendUsePool.discardPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("run {}", finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Thread.currentThread().join(4000);
    }

    /**
     * 使用时提交 Runnable 的自定义实现，附带业务标识，从而实现感知拒绝的具体任务，等待队列中的具体任务。
     */
    @Test
    public void testTaskPool() throws Exception {
        for (int i = 0; i < 20; i++) {
            int finalI = i + 1;
            RecommendUsePool.discardPool.execute(new RecommendUsePool.Task("task-" + finalI, () -> {
                try {
                    Thread.sleep(1000);
                    log.info("run task-{}", finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        Thread.currentThread().join(4000);
    }

    /**
     * 测试消费批量任务
     * 任务特点：高内存高CPU占用，特殊时段批量创建 但其他时间较空闲
     * 目标：固定并发数的前提下平缓消费，空闲时释放所有线程
     * <p>
     * 限制内存 -Xmx500m
     *
     * @see RecommendUsePoolTest#testBatchTaskPool2() 优化版
     */
    @Test
    public void testBatchTaskPool() throws Exception {
        LinkedBlockingQueue<String> shardQueue = new LinkedBlockingQueue<>(100);

        // mock SpringScheduler or xxl-job quartz
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger consumerCnt = new AtomicInteger();

        AtomicReference<Semaphore> semaphore = new AtomicReference<>(new Semaphore(2, true));

        scheduler.scheduleAtFixedRate(() -> {
            try {
                log.info("check");
                int batch = consumerCnt.incrementAndGet();

                // 如果小任务居多 大任务穿插出现，可以将expect适当调大 提高整体执行效率
                int expect = semaphore.get().availablePermits() + 1;
                for (int i = 0; i < expect; i++) {
                    // 此处换成MySQL或Redis 即可实现集群方式跑任务
                    String task = shardQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (Objects.isNull(task)) {
                        return;
                    }

                    semaphore.get().acquire();
                    RecommendUsePool.limitPool.execute(() -> {
                        try {
                            // mock
                            byte[] cache = new byte[10 * 1024 * 1024];
                            TimeUnit.SECONDS.sleep(4 + ThreadLocalRandom.current().nextInt(30));
                        } catch (InterruptedException e) {
                            log.error("", e);
                            throw new RuntimeException(e);
                        } finally {
                            semaphore.get().release();
                        }
                        log.info("batch={} task={}", batch, task);
                    });
                }
            } catch (Exception e) {
                log.error("", e);
            }
            // 实际可能是1min 或 30s 取任务执行耗时的中位数
        }, 10, 10, TimeUnit.SECONDS);

        Blade.create()
                .listen(33388)
                // 创建任务
                .get("/create", ctx -> addTask(ctx, counter, shardQueue))
                // 调整并发值
                .get("/con", ctx -> {
                    Semaphore cache = semaphore.get();

                    if (cache.getQueueLength() > 0 || RecommendUsePool.limitPool.getActiveCount() > 0) {
                        ctx.text("Still Run");
                        return;
                    }
                    Optional<String> c = ctx.request().query("c");
                    c.map(Integer::parseInt).ifPresent(v -> {
                        semaphore.set(new Semaphore(v, true));
                        ctx.text("Set " + v);
                    });
                })
                .start(Application.class);

        Thread.currentThread().join();
    }

    /**
     * 简化限流由最大线程数控制
     */
    @Test
    public void testBatchTaskPool2() throws Exception {
        LinkedBlockingQueue<String> shardQueue = new LinkedBlockingQueue<>(100);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger consumerCnt = new AtomicInteger();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                int batch = consumerCnt.incrementAndGet();

                // 由于扫描的时候可能有线程的任务还有一会才结束，即线程池最坏情况是所有线程休息一个扫描时间片段，最佳情况是扫描片段内完成了相应任务
                // 短的扫描间隔适合大量小任务时，提高执行效率
                int expect = RecommendUsePool.limitCachePool.getMaximumPoolSize() - RecommendUsePool.limitCachePool.getActiveCount();
                log.info("check {}", expect);
                if (expect == 0) {
                    return;
                }

                for (int i = 0; i < expect; i++) {
                    String task = shardQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (Objects.isNull(task)) {
                        return;
                    }
                    RecommendUsePool.limitCachePool.execute(() -> {
                        try {
                            byte[] cache = new byte[10 * 1024 * 1024];
                            TimeUnit.SECONDS.sleep(4 + ThreadLocalRandom.current().nextInt(10));
                        } catch (InterruptedException e) {
                            log.error("", e);
                            throw new RuntimeException(e);
                        }
                        log.info("batch={} task={}", batch, task);
                    });
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }, 10, 10, TimeUnit.SECONDS);

        Blade.create()
                .listen(33388)
                .get("/create", ctx -> addTask(ctx, counter, shardQueue))
                .get("/con", ctx -> {
                    Optional<String> c = ctx.request().query("c");
                    c.map(Integer::parseInt).ifPresent(v -> {
                        RecommendUsePool.limitCachePool.setMaximumPoolSize(v);
                        ctx.text("Set " + v);
                    });
                })
                .start(Application.class);

        Thread.currentThread().join();
    }

    private static void addTask(RouteContext ctx, AtomicInteger counter, LinkedBlockingQueue<String> shardQueue) {
        Optional<String> c = ctx.request().query("c");
        int count = c.map(Integer::parseInt).orElse(10);
        int batch = counter.incrementAndGet();
        for (int i = 0; i < count; i++) {
            try {
                shardQueue.put(batch + "-" + i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ctx.text("add " + count + " queue:" + shardQueue.size());
    }


    @Test
    public void testEmptyCoreThread() throws Exception {
        RecommendUsePool.coreCachePool.getActiveCount();
        Thread.currentThread().join();
    }

    @Test
    public void testKillCoreThread() throws Exception {
        Thread.sleep(3000);
        log.info("start");
        final int total = 500;
        final int taskTime = 1000;
        final int submitTime = 20;

        for (int i = 0; i < total; i++) {
            if (i < 10) {
                Thread.sleep(taskTime);
            } else {
                // 注意当此处提交任务的频率 当 taskTime/submitTime < core 时任务消费正常，当大于core就会开始使用到临时线程, 当大于max以及队列满时就会开始丢弃任务了
                Thread.sleep(submitTime);
            }
            RecommendUsePool.coreCachePool.execute(new RecommendUsePool.Task("task-" + i, () -> {
                try {
                    Thread.sleep(taskTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        Thread.currentThread().join(10000);
        // 此时已经销毁了所有的core线程（大于idle的8s），下面的任务会开启新的core
        RecommendUsePool.coreCachePool.execute(() -> {
            try {
                Thread.sleep(10000);
                log.info("Last task");
            } catch (Exception e) {
                log.error("", e);
            }
        });
        Thread.currentThread().join(15000);
        log.info("end");
    }


    /**
     * @see CompletableFutureTest
     */
    @Test
    public void testTmpNewAsyncPool() throws Exception {
        TimeUnit.SECONDS.sleep(10);
        int taskNum = 50;
        ExecutorService mainPool = Executors.newFixedThreadPool(1);

        mainPool.execute(() -> {
            ThreadPoolExecutor coreCachePool = new ThreadPoolExecutor(4, 4,
                    8L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(taskNum),
                    new BasicThreadFactory.Builder().namingPattern("tmp-%d").build(),
                    new RecommendUsePool.TrackDiscardPolicy());
            List<CompletableFuture<Void>> wait = new ArrayList<>();

            try {
                for (int i = 0; i < taskNum; i++) {
                    int finalI = i;
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        log.info("sleep start {}", finalI);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            log.error("", e);
                        }
                        log.info("end {}", finalI);
                    }, coreCachePool);
                    wait.add(future);
                }

                CompletableFuture[] all = wait.stream().toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(all).join();
            } catch (Exception e) {
                log.error("", e);
            } finally {
                log.info("shutdown");
                coreCachePool.shutdown();
            }
            log.info("Finish All");
        });

        log.info("start");
        Thread.currentThread().join();
    }
}
