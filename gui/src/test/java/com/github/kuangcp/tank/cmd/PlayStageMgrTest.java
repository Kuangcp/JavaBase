//package com.github.kuangcp.tank.v3;
//
//import co.paralleluniverse.fibers.DefaultFiberScheduler;
//import co.paralleluniverse.fibers.FiberScheduler;
//import org.junit.Test;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-12 02:05
// */
//public class PlayStageMgrTest {
//
//    @Test
//    public void testFiber() throws Exception {
//        int max = 200;
//        final CountDownLatch latch = new CountDownLatch(max);
//
//        final long start = System.currentTimeMillis();
//        final FiberScheduler scheduler = DefaultFiberScheduler.getInstance();
//
////        final FiberForkJoinScheduler fiberForkJoinScheduler = new FiberForkJoinScheduler("xx", 3, null, false);
////        fiberForkJoinScheduler.getForkJoinPool().shutdown();
//        for (int i = 0; i < max; i++) {
//            int finalI = i;
//            try {
//
//                final Runnable runnable = () -> {
//                    try {
//                        TimeUnit.SECONDS.sleep(3);
//                    } catch (InterruptedException e) {
//                        log.error("", e);
//                    }
//                    System.out.println("Inside fiber coroutine..." + finalI);
//                    latch.countDown();
//                };
//
////                dd.execute(runnable);
//                scheduler.getExecutor().execute(runnable);
////                new Fiber<Void>(runnable).start();
//            } catch (Exception e) {
//            }
//        }
//
//
//        latch.await();
//        System.out.println("---------------------");
//        System.out.println("---------------------");
//        System.out.println("---------------------");
//        System.out.println("---------------------");
//        System.out.println("---------------------");
//        System.out.println("---------------------");
//        System.out.println(System.currentTimeMillis() - start);
//
//    }
//}