package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kuangcp
 * 2024-05-15 09:58
 */
@Slf4j
public class RecommendUsePool {

    /**
     * 测试自定义拒绝策略
     * 测试自定义runnable
     */
    public static ThreadPoolExecutor discardPool = new ThreadPoolExecutor(2, 5, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5), new TrackDiscardPolicy());

    /**
     * 限制最高并发 批量处理任务
     * <p>
     * max 非业务值 仅仅为安全值
     */
    public static ThreadPoolExecutor limitPool = new ThreadPoolExecutor(0, 20,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new BasicThreadFactory.Builder().namingPattern("limit-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * max为业务实际值即最大并发数
     */
    public static ThreadPoolExecutor limitCachePool = new ThreadPoolExecutor(0, 3,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new BasicThreadFactory.Builder().namingPattern("limit-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * IO密集型线程池 快速消费任务和快速回收线程
     * <p>
     * 待执行的任务一旦达到max就会被丢弃，因为队列是零空间的阻塞实现
     */
    public static ThreadPoolExecutor cachePool = new ThreadPoolExecutor(0, 100,
            30L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new BasicThreadFactory.Builder().namingPattern("nio-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * IO密集型线程池 快速消费快速回收具有超量任务缓冲的能力
     * <p>
     * 任务总数量小于core时 提交后都会立刻有线程响应，超过core部分会进入队列等待，队列满了就会创建新线程直到达到max 然后丢弃后续的任务
     * Executors.newCachedThreadPool 实现是无限线程数，具有风险。
     */
    public static ThreadPoolExecutor coreCachePool = new ThreadPoolExecutor(20, 50,
            8L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50),
            new BasicThreadFactory.Builder().namingPattern("react-%d").build(),
            new TrackDiscardPolicy());

    static {
        // 默认不会回收idle超时的 core线程 ，只会回收 core到max部分的临时线程，设置该值以节省资源
        coreCachePool.allowCoreThreadTimeOut(true);
    }

    /**
     * 自定义拒绝策略，记录拒绝次数
     */
    public static class TrackDiscardPolicy extends ThreadPoolExecutor.DiscardPolicy {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            final int cnt = counter.incrementAndGet();
            if (r instanceof Task) {
                Task task = (Task) r;
                log.error("rejectTask {} {} {}", task.id, cnt, e);
            } else {
                log.error("rejectRun {} {}", cnt, e);
            }
            super.rejectedExecution(r, e);
        }
    }

    /**
     * 定制 Runnable 任务，传递业务信息
     */
    public static class Task implements Runnable {

        private final String id;
        private final Runnable task;

        public Task(String id, Runnable task) {
            this.id = id;
            this.task = task;
        }

        @Override
        public void run() {
            this.task.run();
        }
    }


}
