package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author kuangcp on 2019-04-19 9:07 PM
 */
@Slf4j
public class InterruptHandleDemo {

    public static void main(String[] args) throws InterruptedException {
        log.info("start");
        Thread sleep = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("PRE");
                    // 如果 isInterrupted 为true，sleep会直接抛出 InterruptedException
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("RUN");
                } catch (InterruptedException e) {
                    // 一旦 InterruptedException 异常被catch过，isInterrupted 就会被清除，设置为false
                    log.error(e.getMessage(), e);
                    log.info("state: {}", Thread.currentThread().isInterrupted());

                    // 可以再次设置 isInterrupted 为true， 但是会导致循环会快速执行很多次因为sleep会立即抛异常
                    Thread.currentThread().interrupt();
                    log.info("state: {}", Thread.currentThread().isInterrupted());

                    // 如果执行了该方法，isInterrupted 就会被清除，设置为false，循环依旧会正常执行，前文的 interrupt 相当于无效了
                    log.info("state: {}", Thread.interrupted());
                }
            }
        }, "Sleep");
        // 由于该线程调用阻塞代码后catch了异常，同样不会停止，同样需要设置为守护线程
        sleep.setDaemon(true);


        Thread busy = new Thread(() -> {
            while (true) {
                Math.tan(111111111111.11);
            }
        }, "Busy");
        // 由于该线程是高度占用CPU，interrupt 只是标记了状态，此线程没有阻塞代码的调用，没有机会抛出异常从而停止，所以会一直运行下去，
        // 只有设置为守护线程，才能让Main线程不会join等待此线程执行结束，否则Main线程会一直执行下去
        busy.setDaemon(true);

        sleep.start();
        busy.start();

        TimeUnit.SECONDS.sleep(2);
        sleep.interrupt();
        busy.interrupt();
        log.info("sleep {}", sleep.isInterrupted());
        log.info("busy  {}", busy.isInterrupted());

        Thread.currentThread().join(8000);
    }
}
