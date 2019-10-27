package com.github.kuangcp.time.wheel;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * https://github.com/wolaiye1010/zdc-java-script
 *
 * 类似于 HashMap的设计结构
 * 设计支持 30 天内延迟任务
 *
 * @author https://github.com/kuangcp on 2019-10-11 20:53
 */
@Slf4j
public class TimeWheel {

  private volatile boolean stop = false;
  private volatile boolean needExitVM = false;

  private volatile long startEmptyTime = -1;
  private volatile long maxTimeout = 10L;

  private static final int SECONDS_SLOT = 60;
  private static final int MINUTES_SLOT = 60;
  private static final int HOURS_SLOT = 24;
  private static final int DAYS_SLOT = 30;

  private AtomicInteger currentSecond = new AtomicInteger(0);
  private AtomicInteger currentMinute = new AtomicInteger(0);
  private AtomicInteger currentHour = new AtomicInteger(0);
  private AtomicInteger currentDay = new AtomicInteger(0);

  private ExecutorService pool = Executors.newFixedThreadPool(6);
  private ExecutorService master = Executors.newSingleThreadExecutor();

  // id -> task
  private final Map<String, Callable<?>> cacheTasks = new ConcurrentHashMap<>();
  private final Map<ChronoUnit, LinkedList[]> wheels = new ConcurrentHashMap<>();

  {
    LinkedList[] secondsSlot = new LinkedList[SECONDS_SLOT];
    wheels.put(ChronoUnit.SECONDS, secondsSlot);
    LinkedList[] minutesSlot = new LinkedList[MINUTES_SLOT];
    wheels.put(ChronoUnit.MINUTES, minutesSlot);
    LinkedList[] hoursSlot = new LinkedList[HOURS_SLOT];
    wheels.put(ChronoUnit.HOURS, hoursSlot);
    LinkedList[] daysSlot = new LinkedList[DAYS_SLOT];
    wheels.put(ChronoUnit.DAYS, daysSlot);
  }

  private static class LinkedList {

    private Node head;
    private Node tail;
    private int len;

    boolean add(Node node) {
      len++;
      if (Objects.isNull(head)) {
        head = node;
        tail = node;
        return true;
      }

      this.tail.next = node;
      this.tail = node;
      return true;
    }

    void clear() {
      len = 0;
      this.head = null;
      this.tail = null;
    }

    List<Node> toList() {
      List<Node> result = new ArrayList<>(len);
      Node pointer = this.head;
      while (Objects.nonNull(pointer)) {
        result.add(pointer);
        pointer = pointer.next;
      }
      return result;
    }

    String toSimpleString() {
      List<Node> nodes = toList();
      if (nodes.isEmpty()) {
        return "";
      }
      return nodes.stream().map(Node::getId).collect(Collectors.joining(","));
    }

    boolean isEmpty() {
      return Objects.isNull(head);
    }
  }

  public TimeWheel() {
  }

  public TimeWheel(long maxTimeout, boolean needExitVM) {
    this.maxTimeout = maxTimeout;
    this.needExitVM = needExitVM;
  }

  @Data
  private static class Node {

    private String id;
    private Node next;
    private long runTime;

    Node(String id, Node next, long runTime) {
      this.id = id;
      this.next = next;
      this.runTime = runTime;
    }

  }


  public void start() {
    master.execute(() -> {
      while (!stop) {
        log.debug("cache: keys={}", cacheTasks.keySet());
        if (cacheTasks.isEmpty()) {
          long current = System.currentTimeMillis();
          if (startEmptyTime == -1) {
            startEmptyTime = current;
          } else if (current - startEmptyTime > maxTimeout) {
            log.warn("no any tasks with timeout");
            shutdown();
            continue;
          }
        }

        try {
          TimeUnit.SECONDS.sleep(1);

          int seconds = this.currentSecond.incrementAndGet();
          this.rushLoop(seconds);

          LinkedList[] lists = wheels.get(ChronoUnit.SECONDS);
          LinkedList list = lists[seconds % SECONDS_SLOT];
          this.invokeAll(seconds, list);

        } catch (Exception e) {
          log.error("", e);
        }
      }
    });
  }

  private void removeAndAdd(ChronoUnit unit, int index) {
    LinkedList[] lists = wheels.get(unit);
    LinkedList list = lists[index];
    if (Objects.nonNull(list) && !list.isEmpty()) {
      List<Node> nodes = list.toList();
      list.clear();
      for (Node node : nodes) {
        long millis = node.runTime - System.currentTimeMillis();
        if (millis < 0) {
          log.error("system lose task: node={}", node);
//          cacheTasks.remove(node.getId());
//          continue;
        }

        Duration delay = Duration.ofMillis(millis);
        log.debug(": delay={}", delay);
        insertWheel(node.getId(), delay);
      }
    }
  }

  private void rushLoop(int seconds) {
    int costMin = -1;
    int costHour = -1;
    int costDay = -1;

    if (seconds >= SECONDS_SLOT) {
      this.currentSecond.set(0);
      costMin = this.currentMinute.incrementAndGet();
    }
    if (costMin > MINUTES_SLOT) {
      this.currentMinute.set(0);
      costHour = this.currentHour.incrementAndGet();
    }
    if (costHour > HOURS_SLOT) {
      this.currentHour.set(0);
      costDay = this.currentDay.incrementAndGet();
    }

    if (costDay > DAYS_SLOT) {
      this.currentDay.set(0);
      this.removeAndAdd(ChronoUnit.DAYS, 0);
      costDay = -1;
    }

    if (costDay != -1) {
      this.removeAndAdd(ChronoUnit.DAYS, costDay);
    }

    if (costHour != -1) {
      this.removeAndAdd(ChronoUnit.HOURS, costHour);
    }

    if (costMin != -1) {
      this.removeAndAdd(ChronoUnit.MINUTES, costMin);
    }
  }

  private void invokeAll(int seconds, LinkedList list) {
    if (Objects.isNull(list) || list.isEmpty()) {
      log.warn("no tasks need invoke");
      return;
    }

    List<Node> nodes = list.toList();
    for (Node node : nodes) {
      String id = node.getId();
      Callable<?> func = cacheTasks.get(id);
      log.debug("[{}]before invoke: id={}", seconds, id);
      try {
        Future<?> result = pool.submit(func);
        log.info("[{}]invoke: id={} result={}", seconds, id, result.get());
        cacheTasks.remove(id);
      } catch (Exception e) {
        log.error("", e);
      }
    }
    list.clear();
    log.info("[{}]invoke all tasks successful.", seconds);
  }

  public void shutdown() {
    this.stop = true;
    log.warn("shutdown");
    if (needExitVM) {
      System.exit(1);
    }
  }

  public boolean add(String id, Callable<?> func, Duration delay) {
    if (Objects.isNull(delay)) {
      log.warn("delay is null: id={}", id);
      return false;
    }
    if (Objects.isNull(func)) {
      log.warn("func is null: id={}", id);
      return false;
    }

    if (cacheTasks.containsKey(id)) {
      log.warn("task has already exist");
      return false;
    }

    boolean result = insertWheel(id, delay);
    if (result) {
      cacheTasks.put(id, func);
    }
    return result;
  }

  public void printWheel() {
    for (ChronoUnit unit : wheels.keySet()) {
      LinkedList[] lists = wheels.get(unit);
      for (int i = 0; i < lists.length; i++) {
        LinkedList list = lists[i];
        if (Objects.nonNull(list) && !list.isEmpty()) {
          log.info("{} [{}] ids={}", unit.toString(), i, list.toSimpleString());
        }
      }
    }
  }

  private boolean insertWheel(String id, Duration delay) {
    long days = delay.toDays();
    if (days > 30) {
      log.warn("out of timeWheel max delay bound");
      return false;
    }

    long runTime = delay.toMillis() + System.currentTimeMillis();
    if (days > 0) {
      return insertWheel(ChronoUnit.DAYS, days, id, runTime);
    }

    long hours = delay.toHours();
    if (hours > 0) {
      return insertWheel(ChronoUnit.HOURS, hours, id, runTime);
    }

    long minutes = delay.toMinutes();
    if (minutes > 0) {
      return insertWheel(ChronoUnit.MINUTES, minutes, id, runTime);
    }

    long seconds = delay.getSeconds();
    // TODO expire
    if (seconds >= -2 && seconds <= 0) {
      return insertWheel(ChronoUnit.SECONDS, 0, id, runTime);
    }
    if (seconds > 0) {
      return insertWheel(ChronoUnit.SECONDS, seconds, id, runTime);
    }

    log.warn("task is expire: id={} delay={}", id, delay);
    return false;
  }

  private boolean insertWheel(ChronoUnit unit, long index, String id, long runTime) {
    int idx = (int) index;
    LinkedList[] lists = wheels.get(unit);
    LinkedList list = lists[idx];
    if (Objects.isNull(list)) {
      list = new LinkedList();
      lists[idx] = list;
    }
    return list.add(new Node(id, null, runTime));
  }
}
