package com.github.kuangcp.time.wheel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-10-27 12:15
 */
@Slf4j
public class TimeWheelTest {

  private TimeWheel timeWheel = new TimeWheel(3000L, true);

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
  public void testRun2() {
    for (int i = 0; i < 13; i++) {
      boolean result = timeWheel.add("id" + i, () -> "fds", Duration.ofMillis(5000));
      log.info(": result={}", result);
    }
    timeWheel.start();

    try {
      TimeUnit.SECONDS.sleep(10);
      timeWheel.shutdown();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  private long calculate() {
    runRecord.add(System.currentTimeMillis());

    OptionalInt reduce = IntStream.rangeClosed(0, 990000).reduce(Integer::sum);
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
    for (int i = 1; i < 12; i++) {
      inputData.put("id" + i, i * 10000L);
    }
  }

  private void showRecords() {
    List<Entry<String, Long>> entryList = inputData.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue))
        .collect(Collectors.toList());
    for (int i = 0; i < entryList.size(); i++) {
      Long time = runRecord.get(i);
      log.info("{}:{} out={} runTime={}", entryList.get(i), time - startTime,
          entryList.get(i).getValue() - time + startTime, time);
    }
  }

  // task execute time should << time wheel min slot
  @Test
  public void testRushLoop() {
    initOverMinData();
    for (Entry<String, Long> entry : inputData.entrySet()) {
      boolean result = timeWheel
          .add(entry.getKey(), this::calculateComplex, Duration.ofMillis(entry.getValue()));
      log.info("add task: id={} result={}", entry.getKey(), result);
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
