package com.github.kuangcp.time.wheel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author https://github.com/kuangcp on 2019-10-27 12:15
 */
@Ignore
@Slf4j
public class TimeWheelTest {

    private final TimeWheel timeWheel = new TimeWheel(10L, true, true);

    @Test
    public void testAdd() throws Exception {
        boolean result = timeWheel.add("id", () -> null, Duration.ofMillis(10000));
        Assert.assertTrue(result);
    }

    @Test
    public void testRun1() {
        for (int i = 0; i < 7; i++) {
            boolean result = timeWheel.add("id" + i, () -> null, Duration.ofMillis(i * 3000));
            log.debug(": result={}", result);
        }
        timeWheel.start();
        join();
    }

    private void join() {
        try {
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testRun2() throws InterruptedException {
        Callable<Object> longCall = () -> {
            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                Supplier<Integer> random = () -> new SecureRandom().nextInt(10000000) + 100;
                Math.tanh(Math.log(Math.sqrt(random.get()) + random.get()));
            }
            long end = System.nanoTime();

            return (end - start) + "";
        };

        TimeWheel timeWheel = new TimeWheel(10L, false, true);
        for (int x = 0; x < 50; x++) {
            boolean result = timeWheel.add("id-" + x, longCall, Duration.ofMillis(5000));
            if (!result) {
                log.info("add failed");
            }
        }
        timeWheel.start();

        timeWheel.blockWait();
        log.info("finish");
    }

    private long calculate() {
        runRecord.add(System.currentTimeMillis());

        OptionalInt reduce = IntStream.rangeClosed(0, 200).reduce(Integer::sum);
        return reduce.orElse(0);
    }

    private long calculateComplex() {
        runRecord.add(System.currentTimeMillis());

        Optional<Long> sumOpt = IntStream.rangeClosed(0, 990000)
                .mapToObj(Long::valueOf)
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .sorted(Comparator.comparing(Long::intValue))
                .sorted(Comparator.comparing(Long::intValue).reversed())
                .reduce(Long::sum);
        return sumOpt.orElse(0L);
    }

    // id -> delay mills
    private Map<String, Long> inputData = new HashMap<>();
    // id -> start run mills
    private List<Long> runRecord = new ArrayList<>();
    private long startTime;

    private void initOverMinData() {
        int dataSize = 60 * 60 * 2;
        dataSize = 10;
        for (int i = 1; i < dataSize; i++) {
            inputData.put("id" + i, i * 65_000L);
        }
    }

    private void showRecords() {
        List<Entry<String, Long>> entryList = inputData.entrySet().stream()
                .sorted(Comparator.comparing(Entry::getValue))
                .collect(Collectors.toList());
        for (int i = 0; i < entryList.size(); i++) {
            Long time = runRecord.get(i);
            Long delayMills = entryList.get(i).getValue();
            log.info("{}:{} out={} runTime={}", entryList.get(i), time - startTime,
                    time - startTime - delayMills, time);
        }
    }

    // task execute time should << time wheel min slot
    @Test
    public void testRushLoop() {
        initOverMinData();
        for (Entry<String, Long> entry : inputData.entrySet()) {
            boolean result = timeWheel
                    .add(entry.getKey(), this::calculate, Duration.ofMillis(entry.getValue()));
            log.debug("add task: id={} result={}", entry.getKey(), result);
        }

        timeWheel.printWheel();
        timeWheel.start();
        startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(this::showRecords));

        new Thread(() -> {
            while (true) {
                timeWheel.printWheel();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }).start();
        join();
    }

}
