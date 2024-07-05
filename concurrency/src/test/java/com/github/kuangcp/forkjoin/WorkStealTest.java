package com.github.kuangcp.forkjoin;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Kuangcp
 * 2024-04-08 09:56
 */
@Slf4j
public class WorkStealTest {

    private final Map<String, AtomicLong> thread = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, AtomicInteger> cnt = new ConcurrentHashMap<>();

    private void spend() {
        long start = System.currentTimeMillis();
        int totalLoop = ThreadLocalRandom.current().nextInt(500000) + 100000;
        for (int i = 0; i < totalLoop; i++) {
            Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt(10000000) + 100;
            Math.tanh(Math.log(Math.sqrt(random.get()) + random.get()));
        }
        long end = System.currentTimeMillis();
//        log.info(" {} ms", end - start);

        String name = Thread.currentThread().getName();
        AtomicLong time = thread.get(name);
        time.addAndGet(end - start);
        cnt.get(name).incrementAndGet();
    }

    @Test
    public void testExecutors() throws Exception {
        ExecutorService pool = Executors.newWorkStealingPool(9);
        compareThreadTask(pool);
    }

    @Test
    public void testNormalPool() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(9);
        compareThreadTask(pool);
    }


    private void compareThreadTask(ExecutorService pool) throws InterruptedException {
        for (int i = 0; i < 20000; i++) {
            int finalI = i;
            pool.execute(() -> {
                String name = Thread.currentThread().getName();
                lock.lock();
                if (!thread.containsKey(name)) {
                    thread.put(name, new AtomicLong());
                    cnt.put(name, new AtomicInteger());
                }
                lock.unlock();
//                log.info("work {}", finalI);
                spend();
            });
        }
        ScheduledExecutorService sche = Executors.newScheduledThreadPool(1);
        sche.scheduleAtFixedRate(() -> {
            String all = thread.entrySet().stream().map(v -> v.getKey() + " " + cnt.get(v.getKey()) + " " + v.getValue()).collect(Collectors.joining("\n"));
            log.warn("\n{}", all);
        }, 1, 1, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
