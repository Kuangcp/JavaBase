package situation.timoutpool;


import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kuangcp
 */
@Slf4j
class TimeoutExecPoolTest {

    @Test
    public void testRequest() throws InterruptedException {
//        TimeoutExecPool<String, Long> timeoutPool = new TimeoutExecPool<>(10, Duration.ofSeconds(10));
        TimeoutExecPool<String, Long> timeoutPool = new TimeoutExecPool<>(10, Duration.ofSeconds(5));
        List<String> params = IntStream.range(0, 300).mapToObj(v -> UUID.randomUUID().toString().substring(0, 5) + v).collect(Collectors.toList());
//        log.info("start param={}", params);
        log.info("start");
        List<Long> result = timeoutPool.execute(params, this::logic);
//        log.info("end: result={}", result);
        log.info("end");
//        Thread.currentThread().join();
    }

    private Long logic(String param) {
        if (Objects.isNull(param)) {
            return 0L;
        }
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (long) param.length();
    }
}