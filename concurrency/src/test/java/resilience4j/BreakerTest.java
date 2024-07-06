package resilience4j;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.vavr.control.Try;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.function.Supplier;


/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2022-08-03 19:20
 */
public class BreakerTest {

    private String doAction() {
        return "true";
    }

    @Test
    public void testBreaker() {

// Create a custom RateLimiter configuration
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(100))
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .build();
// Create a RateLimiter
        RateLimiter rateLimiter = RateLimiter.of("backendName", config);

// Decorate your call to BackendService.doSomething()
        Supplier<String> restrictedSupplier = RateLimiter
                .decorateSupplier(rateLimiter, this::doAction);

// First call is successful
        Try<String> firstTry = Try.ofSupplier(restrictedSupplier);
        assert firstTry.isSuccess();
// Second call fails, because the call was not permitted
//        Try<String> secondTry = Try.of(restrictedSupplier);
//        assert secondTry.isFailure();
//        assertThat(secondTry.getCause()).isInstanceOf(RequestNotPermitted.class);
    }
}
