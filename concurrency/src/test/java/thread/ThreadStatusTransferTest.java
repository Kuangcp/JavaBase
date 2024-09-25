package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import thread.ThreadStatusTransfer.Notify;
import thread.ThreadStatusTransfer.Wait;
import thread.ThreadStatusTransfer.BlockMarkWait;

import java.util.concurrent.TimeUnit;

/**
 * @author kuangcp on 2019-04-22 9:40 AM
 */
@Slf4j
public class ThreadStatusTransferTest {

    @Test
    public void testMain() throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "Waiter");
        waitThread.start();

        TimeUnit.SECONDS.sleep(10);

        Thread notifyThread = new Thread(new Notify(), "Notify");
        notifyThread.start();
    }

    /**
     * 效果为 三个线程进入waiting状态，等notify后三个线程抢锁依次执行
     */
    @Test
    public void testMultipleWait() throws Exception {
        Object lock = new Object();

        Runnable taskHandler = () -> {
            synchronized (lock) {
                try {
                    log.info("start wait");
                    lock.wait();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                log.info("finish");
            }
        };
        new Thread(taskHandler).start();
        new Thread(taskHandler).start();
        new Thread(taskHandler).start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (lock) {
                log.info("hold lock. notify");
                lock.notifyAll();
            }
            synchronized (lock) {
                log.info("try lock");
            }
        }).start();

        Thread.currentThread().join(5000);
        log.info("finish All");
    }

    /**
     * 响应中断的方法： 线程进入等待或是超时等待的状态后，调用interrupt方法都是会响应中断的，
     * 即：Object.wait()、Thread.join、Thread.sleep、LockSupport.park的有参和无参方法。
     * <p>
     * 不响应中断的方法：线程进入阻塞状态后，是不响应中断的，等待进入synchronized的方法或是代码块，都是会被阻塞的，此时不会响应中断，
     * 另外还有一个不响应中断的，那就是阻塞在ReentrantLock.lock方法里面的线程，也是不响应中断的，
     * 如果想要响应中断，可以使用ReentrantLock.lockInterruptibly方法。
     */
    @Test
    public void testBlockInterrupt() throws Exception {
        TimeUnit.SECONDS.sleep(6);
        log.info("start");
        Object lock = new Object();

        Runnable taskHandler = () -> {
            synchronized (lock) {
                try {
                    log.info("start wait");
                    lock.wait();
                    log.info("get lock");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                log.info("finish");
            }
        };
        Thread a = new Thread(taskHandler);
        Thread b = new Thread(taskHandler);
        Thread c = new Thread(taskHandler);

        a.start();
        b.start();
        c.start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.info("hold lock. start notify");
                    TimeUnit.MILLISECONDS.sleep(200);
                    lock.notifyAll();
                    log.info("finish notify");
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            synchronized (lock) {
                log.info("try lock");
            }
        }).start();

        Thread kill = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                // 由于中断了所有线程，包括了处于 timed_waiting 的线程也被中断，锁也因此释放，blocked的线程也因此拿到了锁后执行sleep方法抛出中断异常
                // 时间间隔短没法明确感受到 blocked 线程在发出中断信号的那一刻没作出响应
                a.interrupt();
                b.interrupt();
                c.interrupt();
                log.info("finish all interrupt");
            } catch (Exception e) {
                log.error("", e);
            }
        });
        kill.start();

        Thread.currentThread().join(10000);
        log.info("exit");
    }

    @Test
    public void testBlockInterrupt2() throws Exception {
        TimeUnit.SECONDS.sleep(6);
        log.info("start");
        Object lock = new Object();

        BlockMarkWait a = new BlockMarkWait(lock);
        BlockMarkWait b = new BlockMarkWait(lock);
        BlockMarkWait c = new BlockMarkWait(lock);

        a.start();
        b.start();
        c.start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.info("hold lock. start notify");
                    TimeUnit.MILLISECONDS.sleep(200);
                    lock.notifyAll();
                    log.info("finish notify");
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            synchronized (lock) {
                log.info("try lock");
            }
        }).start();

        Thread kill = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                // 加上判断后，只中断blocked的线程，timed_waiting线程不动，就能看到在interrupt的那一刻，blocked线程都是没反应的
                // 只有等waiting线程释放锁后（间隔700ms左右 1000-（500-200））blocked线程获得锁 执行sleep方法时才报错。
                a.tryInterrupt();
                b.tryInterrupt();
                c.tryInterrupt();
                log.info("finish all interrupt");
            } catch (Exception e) {
                log.error("", e);
            }
        });
        kill.start();

        Thread.currentThread().join(10000);
        log.info("exit");
    }
}
