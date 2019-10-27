package com.github.kuangcp.time.wheel;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
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

  private AtomicInteger currentSecond = new AtomicInteger(0);
  private AtomicInteger currentMinute = new AtomicInteger(0);
  private AtomicInteger currentHour = new AtomicInteger(0);
  private AtomicInteger currentDay = new AtomicInteger(0);

  private Map<ChronoUnit, LinkedList[]> wheels = new ConcurrentHashMap<>();

  {
    LinkedList[] secondsSlot = new LinkedList[60];
    wheels.put(ChronoUnit.SECONDS, secondsSlot);
    LinkedList[] minutesSlot = new LinkedList[60];
    wheels.put(ChronoUnit.MINUTES, minutesSlot);
    LinkedList[] hoursSlot = new LinkedList[24];
    wheels.put(ChronoUnit.HOURS, hoursSlot);
    LinkedList[] daysSlot = new LinkedList[30];
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

    boolean isTail() {
      return Objects.isNull(next);
    }

    public Node(String id, Node next, long runTime) {
      this.id = id;
      this.next = next;
      this.runTime = runTime;
    }

  }

  private ExecutorService pool = Executors.newFixedThreadPool(6);
  private ExecutorService master = Executors.newSingleThreadExecutor();

  // id -> task
  private final Map<String, Callable<?>> cacheTasks = new ConcurrentHashMap<>();

  public void start() {
    master.execute(() -> {
      while (!stop) {
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
          Thread.sleep(1000);
          int seconds = this.currentSecond.incrementAndGet();
          push(seconds);

          LinkedList[] lists = wheels.get(ChronoUnit.SECONDS);
          LinkedList list = lists[seconds % 60];
          this.invokeAll(seconds, list);
        } catch (Exception e) {
          log.error("", e);
        }
      }
    });
  }

  private void push(int seconds) {
    if (seconds > 59) {
      this.currentSecond.set(0);
      this.currentMinute.incrementAndGet();
    }
  }

  private void invokeAll(int seconds, LinkedList list) {
    if (Objects.isNull(list) || list.isEmpty()) {
      log.warn("no tasks need invoke");
      return;
    }

    Node pointer = list.head;
    while (Objects.nonNull(pointer)) {
      String id = pointer.getId();
      Callable<?> func = cacheTasks.get(id);
      log.debug("[{}]before invoke: id={}", seconds, id);
      try {
        Future<?> result = pool.submit(func);
        log.info("[{}]invoke: id={} result={}", seconds, id, result.get());
        cacheTasks.remove(id);
      } catch (Exception e) {
        log.error("", e);
      }
      pointer = pointer.next;
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
    boolean result = insertWheel(id, delay);
    if (result) {
      cacheTasks.put(id, func);
    }
    return result;
  }

  private boolean insertWheel(String id, Duration delay) {
    if (cacheTasks.containsKey(id)) {
      log.warn("task has already exist");
      return false;
    }

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
    if (seconds > 0) {
      return insertWheel(ChronoUnit.SECONDS, seconds, id, runTime);
    }

    log.warn("task is expire: id={}", id);
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
