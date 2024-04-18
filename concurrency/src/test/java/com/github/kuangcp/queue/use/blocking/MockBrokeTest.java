package com.github.kuangcp.queue.use.blocking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-04-15 21:58
 */
@Slf4j
public class MockBrokeTest {

    public static class Stopper {

        private static final AtomicBoolean signal = new AtomicBoolean(false);

        public static boolean isStopped() {
            return signal.get();
        }

        public static boolean isRunning() {
            return !signal.get();
        }

        public static void stop() {
            signal.set(true);
        }
    }

    private static final BlockingQueue<String> eventQueue = new LinkedBlockingQueue<>(5000);

    static class TaskWorker extends Thread {
        @Override
        public void run() {

            while (Stopper.isRunning()) {
                try {
                    // if not task , blocking here
                    final String event = eventQueue.take();
                    log.info("event={}", event);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("persist task error", e);
                }
            }
            log.info("Worker stopped");
        }
    }

    @Test
    public void testBroke() throws Exception {

    }
}
