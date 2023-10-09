package situation.timoutpool;

import lombok.extern.slf4j.Slf4j;
import situation.timoutpool.base.TimeoutExecutor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author kuangcp
 */
@Slf4j
public class TimeoutExecPool<P, R> implements TimeoutExecutor<P, R> {

    private final ThreadPoolExecutor pool;
    private Duration duration;

    public TimeoutExecPool(int coreSize, Duration timeout) {
        this.pool = new ThreadPoolExecutor(coreSize, coreSize, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.DiscardPolicy());
        this.duration = timeout;
    }

    @Override
    public List<R> execute(List<P> params, Function<P, R> handler) {
        List<R> results = Collections.synchronizedList(new ArrayList<>());

        for (P p : params) {
            pool.execute(() -> {
                R result = handler.apply(p);
                results.add(result);
            });
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
//                log.info("{} ", pool.getQueue().size());
                if (pool.getQueue().size() == 0) {
                    pool.shutdown();
                    break;
                }
            }
        }).start();

        try {
            final boolean complete = pool.awaitTermination(duration.toNanos(), TimeUnit.NANOSECONDS);
            if (complete) {
//                log.info("complete");
            } else {
                log.warn("not complete. workQueue={}", pool.getQueue().size());
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return results;
    }
}
