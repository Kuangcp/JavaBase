package guava.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-12-07 07:29
 */
@Slf4j
public class BlockTest {
    @Data
    static class LoginEvent {
        private Integer userId;
    }

    @Slf4j
    static class BlockEventListener {

        /**
         * @see Subscriber.SynchronizedSubscriber 实现，提交到线程池执行
         */
        @Subscribe
        public void blockMethod(LoginEvent event) throws InterruptedException {
            log.warn("block: {}", event.getUserId());
            log.warn("block finish {}", event.getUserId());
        }
    }

    @Slf4j
    static class LoginEventListener {

        /**
         * @see com.google.common.eventbus.Subscriber 实现，接收同类事件时需要等待当前对象同步锁
         */
        @Subscribe
        @AllowConcurrentEvents
        public void handleLog(LoginEvent event) throws InterruptedException {
            log.info("event={}", event.getUserId());
            Thread.sleep(30);
            log.info("finish {}", event.getUserId());
        }
    }

    @Test
    public void testBlockAsync() throws Exception {
        final ExecutorService pool = Executors.newFixedThreadPool(3);
        final AsyncEventBus asyncEventBus = new AsyncEventBus(pool);
        // 注册顺序也是事件广播的顺序
        asyncEventBus.register(new LoginEventListener());
        asyncEventBus.register(new BlockEventListener());

        for (int i = 0; i < 7; i++) {
            final LoginEvent event = new LoginEvent();
            event.setUserId(i);
            asyncEventBus.post(event);
            log.info("post {}", i);
        }

        Thread.currentThread().join();
    }
}
