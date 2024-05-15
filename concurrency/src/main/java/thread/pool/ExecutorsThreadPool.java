package thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:44
 * https://www.cnblogs.com/eesijian/p/5871448.html
 */
@Slf4j
public class ExecutorsThreadPool {
    public static ExecutorService pool = Executors.newFixedThreadPool(1);
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /**
     * @see ScheduledThreadPoolExecutor#delayedExecute 所以任务提交的实现
     */
    public static ScheduledExecutorService customScheduler = new CusSchedulePool(1, "schedule-pool-");

    private void baseType() {
        // 创建有缓存功能的线程池 无队列无最大限制 任务会立马创建线程执行，空闲线程1min后回收
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
