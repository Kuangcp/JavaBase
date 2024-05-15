package thread.pool;


import com.hellokaton.blade.Blade;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import web.Application;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
     * 最终会丢弃10个任务，5个任务分配给线程 5个在队列里
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
     * 使用时提交 Runnable 的自定义实现，得以附带业务标识，从而实现感知拒绝，等待队列中的任务。
     */
    @Test
    public void testTaskPool() throws Exception {
        for (int i = 0; i < 20; i++) {
            int finalI = i + 1;
            RecommendUsePool.taskPool.execute(new RecommendUsePool.Task("task-" + finalI, () -> {
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
     * 任务特点：高内存高CPU占用，特殊时段批量创建其他时间较空闲
     * 目标：固定并发数的前提下平缓消费，空闲时释放所有线程
     * <p>
     * 限制内存 -Xmx500m
     */
    @Test
    public void testBatchTaskPool() throws Exception {
        LinkedBlockingQueue<String> shardQueue = new LinkedBlockingQueue<>(100);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger consumerCnt = new AtomicInteger();

        AtomicReference<Semaphore> semaphore = new AtomicReference<>(new Semaphore(2, true));

        scheduler.scheduleAtFixedRate(() -> {
            try {
                log.info("check");
                int batch = consumerCnt.incrementAndGet();

                // 如果小任务居多 大任务穿插出现，可以将expect适当调大 提高整体执行效率
                int expect = semaphore.get().availablePermits();

                for (int i = 0; i < expect; i++) {
                    String task = shardQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (Objects.nonNull(task)) {
                        semaphore.get().acquire();

                        // TODO 此处换成MySQL或Redis 实现集群方式跑任务
                        RecommendUsePool.limitPool.execute(() -> {
                            try {
                                // mock
                                byte[] cache = new byte[100 * 1024 * 1024];
                                TimeUnit.SECONDS.sleep(2 + ThreadLocalRandom.current().nextInt(7));
                                cache[0] = 2;
                                cache[45] = 5;
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } finally {
                                semaphore.get().release();
                            }
                            log.info("batch={} task={}", batch, task);
                        });
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
            // 实际可能是1min 或 30s 取任务执行耗时的中位数
        }, 3, 3, TimeUnit.SECONDS);

        Blade.create()
                .listen(33388)
                // 创建任务
                .get("/create", ctx -> {
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
                })
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
}
