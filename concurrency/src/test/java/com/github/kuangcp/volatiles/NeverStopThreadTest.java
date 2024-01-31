package com.github.kuangcp.volatiles;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-24 9:55 PM
 */
@Slf4j
public class NeverStopThreadTest {

    @Test
    public void testNeverStop() throws Exception {
        NeverStopThread demo = new NeverStopThread();
        Thread thread = new Thread(demo::neverStop);
        thread.start();

        // 如果不加输出, 可能线程都还没创建起来, 这里就直接修改成了 false
        log.info("prepare to stop");
        demo.stop();

        thread.join(5000);
        // 无法立即停止的原因是 JMM的原因 值在自己的缓存里, 所以这里改动了 stop 但是 thread 里的 stop 没有更新
    }

    @Test
    public void testVolatile() throws InterruptedException {
        NeverStopThread demo = new NeverStopThread();
        Thread thread = new Thread(demo::normalStopWithVolatile);
        thread.start();

        // 如果不加输出, 可能线程都还没创建起来, 这里就直接修改成了false
        log.info("prepare to stop");
        demo.stopWithVolatile();

        thread.join();
    }


    @Test
    public void testSleep() throws InterruptedException {
        NeverStopThread demo = new NeverStopThread();
        Thread thread = new Thread(demo::normalStopWithSleep);
        thread.start();

        log.info("prepare to stop");
        demo.stopWithSleep();

        thread.join();
    }
}