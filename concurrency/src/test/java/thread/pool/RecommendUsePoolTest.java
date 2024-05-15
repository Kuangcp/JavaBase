package thread.pool;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.RejectedExecutionHandler;

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
                    log.info("run {}", finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        Thread.currentThread().join(4000);
    }
}
