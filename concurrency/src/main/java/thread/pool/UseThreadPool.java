package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:44
 * https://www.cnblogs.com/eesijian/p/5871448.html
 */
@Slf4j
public class UseThreadPool {
    public static ExecutorService pool = Executors.newFixedThreadPool(1);
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /**
     * @see ScheduledThreadPoolExecutor#delayedExecute 所以任务提交的实现
     */
    public static ScheduledExecutorService customScheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
        /**
         * 通过 execute submit schedule 提交的 Runnable 任务： 手动传递上下文, 捕获异常
         */
        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            if (command == null) {
                throw new RuntimeException("schedule command NPE");
            }
            String traceId = MDC.get("tid");
            return super.schedule(() -> {
                bizWrapperForRun(command, traceId);
            }, delay, unit);
        }

        /**
         * 通过 execute submit schedule 提交的 Callable 任务 手动传递上下文, 捕获异常
         * 由于会返回Future，此时try catch 要throw 异常才能不影响原逻辑，好处是不用担心业务代码漏 get 从而吞异常，缺点是异常栈会打两次日志
         */
        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            if (callable == null) {
                throw new RuntimeException("schedule callable NPE");
            }
            String traceId = MDC.get("tid");
            return super.schedule(() -> bizWrapperForCall(callable, traceId), delay, unit);
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            if (command == null) {
                throw new RuntimeException("schedule callable NPE");
            }
            String traceId = MDC.get("tid");
            return super.scheduleAtFixedRate(() -> {
                bizWrapperForRun(command, traceId);
            }, initialDelay, period, unit);
        }

        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            if (command == null) {
                throw new RuntimeException("schedule callable NPE");
            }
            String traceId = MDC.get("tid");
            return super.scheduleWithFixedDelay(() -> {
                bizWrapperForRun(command, traceId);
            }, initialDelay, delay, unit);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            // 注意这里永远获取不到抛出的异常
            super.afterExecute(r, t);
//            log.warn("After: ", t);
        }
    };

    private static <V> V bizWrapperForCall(Callable<V> callable, String traceId) throws Exception {
        try {
            MDC.put("tid", traceId);
            return callable.call();
        } catch (Throwable e) {
            log.error("", e);
            throw e;
        } finally {
            MDC.remove("tid");
        }
    }

    private static void bizWrapperForRun(Runnable command, String traceId) {
        try {
            MDC.put("tid", traceId);
            command.run();
        } catch (Throwable e) {
            log.error("", e);
            // 只有 execute 这一种情况是不需要上抛的，为了统一逻辑就都上抛了，execute抛出的异常会被忽略
            throw e;
        } finally {
            MDC.remove("tid");
        }
    }

    private void baseType() {
        // 创建有缓存功能的线程池
        ExecutorService a = Executors.newCachedThreadPool();

        // 创建具有固定大小的线程池
        ExecutorService b = Executors.newFixedThreadPool(1);

        // 创建单线程的线程池
        ExecutorService c = Executors.newSingleThreadExecutor();

        // 创建具有定时功能的线程池 指定基本线程池数量, 该线程池的队列是无限队列
        ScheduledExecutorService d = Executors.newScheduledThreadPool(1);

        // 创建单线程的线程池,可指定延迟
        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();

        // 工作窃取线程池 Fork/Join JDK1.8
        ExecutorService f = Executors.newWorkStealingPool();

        // 创建调度作用线程池，可指定延迟
        ScheduledThreadPoolExecutor g = new ScheduledThreadPoolExecutor(1);

        //    a.submit();      提交任务
        //    a.execute();     执行任务
        //    a.shutdown();    关闭线程池, 等待任务执行完成
        //    a.shutdownNow(); 关闭线程池, 立即关闭

    }
}
