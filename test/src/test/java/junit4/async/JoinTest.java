package junit4.async;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-09-24 14:13
 */
public class JoinTest {

    /**
     * 错误使用: 此时新建的线程还没有执行，单元测试线程就已经退出了
     */
    @Test
    public void testThread() throws Exception {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("async by new thread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        System.out.println("finish " + Thread.currentThread().getName());
    }

    /**
     * 显式指定等待的线程
     */
    @Test
    public void testThread2() throws Exception {
        Thread part = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("async by new thread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        part.start();

        // main 线程会等待 part 线程执行结束
        part.join();
        System.out.println("finish " + Thread.currentThread().getName());
    }


    /**
     * 使用 同步组件 阻塞单元测试main线程
     */
    @Test
    public void testThreadPool() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("async by new thread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        System.out.println("finish " + Thread.currentThread().getName());
    }

    @Test
    public void testThreadPool2() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("async by new thread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 阻塞单元测试线程 2s
        Thread.currentThread().join(2000);

        // 将会一直阻塞单元测试线程，直到手动中断该进程，调试耗时不可控的单元测试场景时可使用
//        Thread.currentThread().join();

        System.out.println("finish " + Thread.currentThread().getName());
    }
}
