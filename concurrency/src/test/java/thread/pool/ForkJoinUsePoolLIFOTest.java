package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 测试 栈的默认 LIFO 策略
 *
 * @author Kuangcp
 * 2024-12-05 13:43
 */
@Slf4j
public class ForkJoinUsePoolLIFOTest {

    // TODO 栈出现长阻塞时的响应情况


    // [Fork join并发框架与工作窃取算法剖析-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1512982)

    /**
     * taskset -c 0,1,2 shell（复制自IDE第一行,需要IDE编译成class才会生效新改动）
     *
     * @see ForkJoinPool#externalPush(ForkJoinTask) 为什么任务只堆积在了一个队列上，因为实现中的随机数是取的提交线程，因此所有任务都堆在一个队列上了 但是为什么发生了工作窃取，任务执行顺序还是没变
     */
    @Test
    public void testLIFO() throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("i={}", finalI);
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }

        ScheduledExecutorService sche = Executors.newScheduledThreadPool(1);
        sche.scheduleAtFixedRate(() -> {
            int parallelism = pool.getParallelism();
            long queuedTaskCount = pool.getQueuedSubmissionCount();
            log.info("con={} wait={}", parallelism, queuedTaskCount);
        }, 1, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    /**
     * 加了第二个线程提交任务后，类似LIFO的效果出来了 第二批优先第一批消费
     */
    @Test
    public void testLIFO2() throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("first={}", finalI);
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }

        TimeUnit.SECONDS.sleep(3);
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int finalI = i;
                pool.submit(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        log.info("second={}", finalI);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                });
            }
        }).start();

        ScheduledExecutorService sche = Executors.newScheduledThreadPool(1);
        sche.scheduleAtFixedRate(() -> {
            int parallelism = pool.getParallelism();
            long queuedTaskCount = pool.getQueuedSubmissionCount();
            log.info("con={} wait={}", parallelism, queuedTaskCount);
        }, 1, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    /**
     * 加了更多线程提交任务后，类似LIFO的效果更明显了
     */
    @Test
    public void testLIFO3() throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("first={}", finalI);
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }

        TimeUnit.SECONDS.sleep(3);
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("{}={}", finalI, finalI);
                } catch (Exception e) {
                    log.error("", e);
                }
            })).start();
        }

        ScheduledExecutorService sche = Executors.newScheduledThreadPool(1);
        sche.scheduleAtFixedRate(() -> {
            int parallelism = pool.getParallelism();
            long queuedTaskCount = pool.getQueuedSubmissionCount();
            log.info("con={} wait={}", parallelism, queuedTaskCount);
        }, 1, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    /**
     * 全部新线程提交任务后，总体规律是有序的，局部是无序的，没有LIFO特性
     */
    @Test
    public void testLIFO4() throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    log.info("{}", finalI);
                } catch (Exception e) {
                    log.error("", e);
                }
            })).start();
        }

        ScheduledExecutorService sche = Executors.newScheduledThreadPool(1);
        sche.scheduleAtFixedRate(() -> {
            int parallelism = pool.getParallelism();
            long queuedTaskCount = pool.getQueuedSubmissionCount();
            log.info("con={} wait={}", parallelism, queuedTaskCount);
        }, 1, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }


    @Test
    public void testSSSS() throws Exception {
        System.out.println("xxxxx");
    }
}
