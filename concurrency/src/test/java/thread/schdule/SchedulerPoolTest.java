package thread.schdule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 *
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-30 15:17
 */
@Slf4j
public class SchedulerPoolTest {

    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }

    @Test
    public void testSimple() throws Exception {
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                printException(r, t);
            }

            /**
             * 手动传递上下文
             */
            @Override
            public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
                if (command == null) {
                    throw new RuntimeException("schedule command NPE");
                }
                String tid = MDC.get("tid");
                return super.schedule(() -> {
                    if (StringUtils.isNoneBlank(tid)) {
                        MDC.put("tid", tid);
                    }
                    command.run();
                }, delay, unit);
            }

            @Override
            protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
                return super.decorateTask(runnable, task);
            }
        };

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            MDC.put("tid", "U" + i);
            TimeUnit.SECONDS.sleep(1);
            log.info("SUBMIT {}", finalI);
            pool.schedule(() -> {
                log.info("RUN {} {}", finalI, MDC.get("tid"));
            }, 3, TimeUnit.SECONDS);
        }

        Thread.currentThread().join();
    }
}
