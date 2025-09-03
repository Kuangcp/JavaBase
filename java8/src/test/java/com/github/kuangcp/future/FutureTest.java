package com.github.kuangcp.future;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2025-09-03 10:28
 */
@Slf4j
public class FutureTest {

    // JDK Future 不支持设置回调，Guava和Netty有扩展实现

    /**
     * @see DefaultPromise#addListener(GenericFutureListener) 如果未执行结束加入队列，否则直接执行
     */
    @Test
    public void testNettyCallback() throws Exception {
        long l = System.currentTimeMillis();
        EventExecutorGroup group = new DefaultEventExecutorGroup(4); // 4 threads
        Future<Integer> f = group.submit(() -> {
            log.info("开始执行耗时操作...");
            TimeUnit.SECONDS.sleep(3);
            return 100;
        });

        f.addListener((FutureListener<Object>) objectFuture -> log.info("计算结果:：" + objectFuture.get()));
        log.info("主线程运算耗时:" + (System.currentTimeMillis() - l) + " ms");
        Thread.currentThread().join();
    }

    @Test
    public void testGuavaCallback() throws Exception {
        long start = System.currentTimeMillis();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        ListenableFuture<Integer> future = service.submit(() -> {
            log.info("开始执行耗时操作...");
            TimeUnit.SECONDS.sleep(3);
            return 100;
        });

        Futures.addCallback(future, new FutureCallback<Integer>() {
            public void onSuccess(Integer result) {
                log.info("计算结果:{}", result);
            }

            public void onFailure(Throwable throwable) {
                log.info("异步处理失败 ", throwable);
            }
        }, service);

        log.info("主线程运算耗时:{} ms", System.currentTimeMillis() - start);
        Thread.currentThread().join();
    }

    /**
     * @see com.google.common.util.concurrent.AbstractFuture#complete(com.google.common.util.concurrent.AbstractFuture, boolean) 任务完成后执行所有监听器
     * @see AbstractFuture#addListener(Runnable, Executor) 如果已经执行完了就直接执行回调，没有的话，头插式加入监听器链表（CAS保证并发安全）
     */
    @Test
    public void testGuavaCallbackDelayListen() throws Exception {
        long start = System.currentTimeMillis();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        ListenableFuture<Integer> future = service.submit(() -> {
            log.info("开始执行耗时操作...");
            TimeUnit.SECONDS.sleep(1);
            return 100;
        });

        // 即使处理完成早于添加回调，也能保证会执行回调
        TimeUnit.SECONDS.sleep(4);

        Futures.addCallback(future, new FutureCallback<Integer>() {
            public void onSuccess(Integer result) {
                log.info("计算结果:{}", result);
            }

            public void onFailure(Throwable throwable) {
                log.info("异步处理失败 ", throwable);
            }
        }, service);

        log.info("主线程运算耗时:{} ms", System.currentTimeMillis() - start);
        Thread.currentThread().join();
    }

    @Test
    public void testCompletableCallback() throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("执行耗时操作...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 100;
        });
        completableFuture.whenComplete((result, e) -> {
            log.info("结果：{}", result);
        });
        log.info("主线程运算耗时:" + (System.currentTimeMillis() - start) + " ms");
        Thread.currentThread().join();
    }

    @Test
    public void testComplexCallback() throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("执行耗时操作...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 100;
        });
        completableFuture = completableFuture.thenCompose(i -> CompletableFuture.supplyAsync(() -> {
            log.info("在回调中执行耗时操作...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return i + 100;
        }));
        completableFuture.whenComplete((result, e) -> {
            log.info("计算结果:" + result);
        });

        log.info("主线程运算耗时:" + (System.currentTimeMillis() - start) + " ms");
        Thread.currentThread().join();
    }
}
