package com.github.kuangcp.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
     * @see java.util.concurrent.CompletableFuture.asyncPool
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

    private int func1(int value) {
        return value + 1;
    }

    private int func2(int value) {
        return value + 2;
    }

    // TODO 理解 lettuce 中 Future 的使用
}
