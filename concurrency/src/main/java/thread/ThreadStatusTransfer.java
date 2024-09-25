package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:46
 * 1. 使用 wait notify notifyAll 需要对调用对象加锁
 * 2. 调用 wait 后 线程状态Running变为Waiting, 将当前线程放入对象的等待队列上
 * 3. notify 调用后, 等待线程 wait 处 不会立即返回, 需要等到 notify 这个线程释放锁后才有机会
 * 4. notify 调用后 将等待线程从等待队列放入同步队列, 等待的线程状态会从 Waiting 变成 Blocked
 */
@Slf4j
public class ThreadStatusTransfer {

    private static boolean flag = true;

    private static final Object lock = new Object();

    static class Wait implements Runnable {

        @Override
        public void run() {
            // 如果 synchronized 一个非 final 的变量, 容易发生 当该对象的引用地址更改后, 同步块里的代码可以被并发执行，因为锁的对象发生变化
            synchronized (lock) {
                while (flag) {
                    try {
                        log.info("flag is true: start wait");
                        lock.wait();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                log.info("flag is false. running");
            }
        }
    }

    static class Notify implements Runnable {

        @Override
        public void run() {
            synchronized (lock) {
                log.info("hold lock. notify");
                flag = false;
                // 除非有特殊考虑，一般都是使用notifyAll，避免有线程一直等不到唤醒
//                lock.notify();
                lock.notifyAll();
            }

            // 这一段就是重新获取锁, 会和wait逻辑进行竞争, 所以执行顺序不定
            synchronized (lock) {
                log.info("hold lock again");
            }
        }
    }


    static class BlockMarkWait extends Thread {
        private boolean holdLock = false;
        private final Object lock;

        public BlockMarkWait(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    log.info("start wait");
                    lock.wait();
                    holdLock = true;
                    log.info("get lock");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
            log.info("finish");
            holdLock = false;
        }

        public void tryInterrupt() {
            if (!holdLock) {
                this.interrupt();
            }
        }
    }

}
