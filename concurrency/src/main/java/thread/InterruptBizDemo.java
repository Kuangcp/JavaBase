package thread;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author Kuangcp
 * 2024-04-16 10:23
 */
@Slf4j
public class InterruptBizDemo {

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static final AtomicBoolean run = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        log.info("start");

        singleThreadMode();
//        consumerScheduler();
    }

    /**
     * 保活
     */
    private static void consumerScheduler() throws InterruptedException {
        Thread producer = new Thread(() -> {
            AtomicInteger count = new AtomicInteger();
            while (true) {
                try {
                    Thread.sleep(1000);
                    cpuWork(2000000);
                    int val = count.getAndIncrement();
                    queue.add("" + val);
                    log.info("product {}", val);
                } catch (InterruptedException e) {
                    log.error("", e);
                    break;
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            log.warn("producer stopped");
        });
        producer.setName("producer");

        // 使用定时线程池 解耦 任务和线程的绑定关系，避免大部分异常情况
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = pool.scheduleAtFixedRate(() -> {
            try {
                log.info("queue {}", queue.size());
                String msg = queue.take();
                cpuWork(1000000);
                log.info("msg={}", msg);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);

        producer.start();

        Thread.currentThread().join(10000);
        // 停止方式
        producer.interrupt();
        future.cancel(true);
        pool.shutdown();
    }

    private static void cpuWork(int x) {
        for (int i = 0; i < x; i++) {
            Supplier<Integer> random = () -> new SecureRandom().nextInt(10000000) + 100;
            Math.tanh(Math.log(Math.sqrt(random.get()) + random.get()));
        }
    }

    private static void singleThreadMode() throws InterruptedException {
        Thread normal = new Thread(() -> log.info("normal"));
        normal.setName("normal");
        normal.start();

        Thread producer = new Thread(() -> {
            AtomicInteger count = new AtomicInteger();
            while (run.get()) {
                try {
                    Thread.sleep(2000);
                    int val = count.getAndIncrement();
                    queue.add("" + val);
                    log.info("product {}", val);
                } catch (InterruptedException e) {
                    log.error("", e);
                    break;
                }
            }
            log.warn("producer stopped");
        });
        producer.setName("producer");

        // TODO 什么情况下 consumer线程会莫名down掉
        Thread consumer = new Thread(() -> {
            while (run.get()) {
                try {
                    String msg = queue.take();
                    log.info("msg={}", msg);
                } catch (InterruptedException e) {
                    log.error("", e);
                    break;
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            log.warn("consumer stopped");
        });
        consumer.setName("consumer");

        producer.start();
        consumer.start();

        // main线程留时间给测试线程运行，到时间后main线程就会退出
        Thread.currentThread().join(4000);

        // 停止方式
        run.set(false);
        producer.interrupt();
        consumer.interrupt();
    }
}
