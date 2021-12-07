package guava.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-12-07 07:29
 */
@Slf4j
public class BlockTest {
    @Data
    static class LoginEvent {
        private Integer userId;
    }

    @Slf4j
    static class BlockEventListener {

        @Subscribe
//        @AllowConcurrentEvents
        public void blockMethod(LoginEvent event) throws InterruptedException {
            log.warn("block: {}", event.getUserId());
            if (event.getUserId() == 0) {
                final CountDownLatch latch = new CountDownLatch(5);
                latch.await();
            }
            log.warn("block finish {}", event.getUserId());
        }
    }

    @Slf4j
    static class LoginEventListener {

        @Subscribe
//        @AllowConcurrentEvents
        public void handleLog(LoginEvent event) throws InterruptedException {
            log.info("event={}", event.getUserId());
            Thread.sleep(30);
            log.info("finish {}", event.getUserId());
        }
    }

    /**
     * TODO 从日志上看为什么会阻塞后，还能有部分event被消费
     *
     * @see com.google.common.eventbus.Subscriber#invokeSubscriberMethod(java.lang.Object) 使用注解 @AllowConcurrentEvents 后 消费事件的方式
     * @see com.google.common.eventbus.Subscriber.SynchronizedSubscriber#invokeSubscriberMethod(java.lang.Object)
     */
    @Test
    public void testBlockAsync() throws Exception {
        // TODO 陷入 一个线程 Park，其他线程 Monitor，表现为：整个线程池无法消费任务，队列阻塞满
        final ExecutorService pool = Executors.newFixedThreadPool(3);
        final AsyncEventBus asyncEventBus = new AsyncEventBus(pool);
        // TODO 注册顺序也会影响结果
        asyncEventBus.register(new LoginEventListener());
        asyncEventBus.register(new BlockEventListener());

        for (int i = 0; i < 7; i++) {
            final LoginEvent event = new LoginEvent();
            event.setUserId(i);
            asyncEventBus.post(event);
            log.info("post {}", i);
        }

        TimeUnit.SECONDS.sleep(100);
    }
}
