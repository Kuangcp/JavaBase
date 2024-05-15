package thread.pool;

import lombok.extern.slf4j.Slf4j;

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
     * 测试拒绝策略
     */
    public static ThreadPoolExecutor discardPool = new ThreadPoolExecutor(2, 5, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5), new TrackDiscardPolicy());
    /**
     * 测试自定义runnable
     */
    public static ThreadPoolExecutor taskPool = new ThreadPoolExecutor(2, 5, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5), new TrackDiscardPolicy());
    /**
     * 限制最高并发 批量处理任务
     */
    public static ThreadPoolExecutor limitPool = new ThreadPoolExecutor(0, 20,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public static class TrackDiscardPolicy extends ThreadPoolExecutor.DiscardPolicy {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (r instanceof Task) {
                Task task = (Task) r;
                counter.incrementAndGet();
                log.error("reject {} {}", task.id, e);
            } else {
                log.error("reject {} {} {}", counter.incrementAndGet(), r, e);
            }
            super.rejectedExecution(r, e);
        }
    }

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
