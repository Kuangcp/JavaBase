package situation.timoutpool;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
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

    /**
     * 可行方案： 使用 ExecutorCompletionService 和 CompletableFuture 组合限制执行时间，汇聚执行结果，取消后续任务
     */
    @Test
    public void testCompleteService() {
        log.info("start");
        CompletionService<Long> cs = new ExecutorCompletionService<>(Executors.newFixedThreadPool(3));

        int max = 10;
        List<String> params = IntStream.range(0, max)
                .mapToObj(v -> UUID.randomUUID().toString() + v)
                .collect(Collectors.toList());
        List<Future<Long>> futures = params.stream().map(v -> cs.submit(() -> this.logic(v))).collect(Collectors.toList());

        CompletableFuture<List<Long>> future = CompletableFuture.supplyAsync(() -> {
            List<Long> results = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                Future<Long> take;
                try {
                    take = cs.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Long result;
                try {
                    result = take.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                log.info("{}", result);
                results.add(result);
            }
            return results;
        });
        try {
            List<Long> results = future.get(80000, TimeUnit.MILLISECONDS);
            log.info("{}", results);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("timeout", e);
            for (Future<Long> longFuture : futures) {
                longFuture.cancel(true);
            }
        }
    }

    /**
     * 保留部分任务执行数据 中断后续任务
     */
    @Test
    public void testCompleteServicePart() {
        log.info("start");
        CompletionService<Long> cs = new ExecutorCompletionService<>(Executors.newFixedThreadPool(2));

        int max = 10;
        List<String> params = IntStream.range(0, max)
                .mapToObj(v -> UUID.randomUUID().toString() + v)
                .collect(Collectors.toList());
        List<Future<Long>> futures = params.stream().map(v -> cs.submit(() -> this.logic(v))).collect(Collectors.toList());

        Vector<Long> vs = new Vector<>();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < max; i++) {
                Future<Long> take;
                try {
                    take = cs.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Long result;
                try {
                    result = take.get();
                    vs.add(result);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                log.info("{}", result);
            }
        });
        log.info("wait result");
        try {
            future.get(800, TimeUnit.MILLISECONDS);
            log.info("{}", vs);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("timeout", e);
            log.warn("part： {}", vs);
            for (Future<Long> longFuture : futures) {
                longFuture.cancel(true);
            }
        }
    }

    private Long logic(String param) {
        if (Objects.isNull(param)) {
            return 0L;
        }
        try {
            Thread.sleep(350);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return (long) param.length();
    }
}