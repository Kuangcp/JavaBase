package guava.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-08-30 03:00
 */
@Slf4j
public class RateLimiterTest {

    @Test
    public void testLimit1() throws Exception {
        final RateLimiter rateLimiter = RateLimiter.create(3, 5, TimeUnit.SECONDS);
        rateLimiter.acquire();

        // SmoothRateLimiter
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    rateLimiter.acquire();
                    log.info("run");
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }).start();

        TimeUnit.HOURS.sleep(1);
    }
}
