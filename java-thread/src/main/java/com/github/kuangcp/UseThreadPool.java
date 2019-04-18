package com.github.kuangcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:44
 * TODO 线程池
 * https://www.cnblogs.com/eesijian/p/5871448.html
 * Executors
 */
public class UseThreadPool {

  private void baseType() {
    // 创建有缓存功能的线程池
    ExecutorService a = Executors.newCachedThreadPool();

    // 创建具有固定大小的线程池
    ExecutorService b = Executors.newFixedThreadPool(1);

    // 创建单线程的线程池
    ExecutorService c = Executors.newSingleThreadExecutor();

    // 创建具有定时功能的线程池 指定基本线程池数量, 该线程池的队列是无限队列
    ScheduledExecutorService d = Executors.newScheduledThreadPool(1);

    ///////////////

    // 创建单线程的线程池,指定延迟
    ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();

//    a.submit();      提交任务
//    a.execute();     执行任务
//    a.shutdown();    关闭线程池, 等待任务执行完成
//    a.shutdownNow(); 关闭线程池, 立即关闭

  }
}
