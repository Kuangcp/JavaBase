package com.github.kuangcp.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Slf4j
public class CompletableFutureTest {

    @Test
    public void testAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("sleep");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            log.info("temp");
        });

        future.get(5, TimeUnit.SECONDS);
        if (future.isDone()) {
            log.info("complete");
        }
    }

    /**
     * @see java.util.concurrent.CompletableFuture.asyncPool 默认线程池
     */
    @Test
    public void testAsyncPoolSize() throws Exception {
        List<Future<Void>> wait = new ArrayList<>();

        for (int i = 0; i < 300; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                log.info("sleep");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
                log.info("temp");
            });
            wait.add(future);
        }
        for (Future<Void> future : wait) {
            future.get();
            if (future.isDone()) {
                log.info("complete");
            }
        }
    }

    @Test
    public void testFutureNeedOptimize() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int num = 3;

        CompletableFuture<Integer> func1Future = new CompletableFuture<>();
        executorService.submit(() -> func1Future.complete(func1(num)));
        int b = func2(num);
        int result = b + func1Future.get();
        System.out.println(result);
        assertThat(result, equalTo(9));
        executorService.shutdown();
    }

    @Test
    public void testFutureAndCombine() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int num = 3;

        CompletableFuture<Integer> func1Future = new CompletableFuture<>();
        CompletableFuture<Integer> func2Future = new CompletableFuture<>();

        // 只会在两个函数都完成计算后进行 避免get() 产生阻塞，并发的损失和死锁问题
        // CompletableFuture 和 结合器 一起使用
        CompletableFuture<Integer> resultFuture = func1Future.thenCombine(func2Future, Integer::sum);

        executorService.submit(() -> func1Future.complete(func1(num)));
        executorService.submit(() -> func2Future.complete(func2(num)));

        Integer result = resultFuture.get();
        System.out.println(result);
        assertThat(result, equalTo(9));

        executorService.shutdown();
    }

    @Test
    public void testSyncException() throws Exception {
        try {
            CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
                if (System.currentTimeMillis() % 1000 < 500) {
                    throw new RuntimeException("invalid");
                }
                return "first";
            });

            CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> {
                if (System.currentTimeMillis() % 1000 < 300) {
                    throw new RuntimeException("invalid");
                }
                return "second";
            });

            CompletableFuture<Void> all = CompletableFuture.allOf(first, second);
            all.get();

            // 异步代码块中发生异常后会包装为 ExecutionException 在 get()时抛出
            log.info("end: {} {} ", first.get(), second.get());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private int func1(int value) {
        return value + 1;
    }

    private int func2(int value) {
        return value + 2;
    }

    // TODO 理解 lettuce 中 Future 的使用


    /**
     * TODO 可控耗时自旋
     */
    @Test
    public void testSpinWait() throws Exception {
        log.info("start");
        CompletableFuture<Void> first = CompletableFuture.runAsync(() -> {
            boolean enable = this.enableRun();
            while (!enable) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ignored) {
                }
                enable = enableRun();
            }
        });


        try {
            first.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("timeout", e);
        }
        log.info("finish");
//        first.thenCombine();
    }

    private final LocalTime now = LocalTime.now();

    private boolean enableRun() {
        log.info("check");
        LocalTime now1 = LocalTime.now();
        return now1.isAfter(now.plusSeconds(10));
    }

    @Test
    public void testNow() throws Exception {
        // 1726284853554
        // 1685014528
        System.out.println(System.currentTimeMillis());
    }
}
