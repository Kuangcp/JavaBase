package com.github.kuangcp.time.wheel;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://github.com/wolaiye1010/zdc-java-script
 * <p>
 * 类似于 HashMap的设计结构 设计支持 30 天内延迟任务
 *
 * @author https://github.com/kuangcp on 2019-10-11 20:53
 */
@Slf4j
public class TimeWheel {

    private boolean debugMode = false;
    private volatile boolean stop = false;
    private final Object lock = new Object();
    private volatile boolean needExitVM = false;

    private volatile long startEmptyTime = -1;
    private volatile long maxTimeout = 10L;
    private volatile long lastCheckMills;

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
    private final Map<ChronoUnit, LinkedList<TaskNode>[]> wheels = new ConcurrentHashMap<>();
    private final Map<ChronoUnit, AtomicInteger> counters = new ConcurrentHashMap<>();
    private final Map<ChronoUnit, Integer> slots = new ConcurrentHashMap<>();
    private final List<ChronoUnit> sortedSlots = Arrays
            .asList(ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.DAYS);

    {
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        LinkedList[] secondsSlot = new LinkedList[SECONDS_SLOT];
        wheels.put(ChronoUnit.SECONDS, secondsSlot);
        counters.put(ChronoUnit.SECONDS, currentSecond);
        slots.put(ChronoUnit.SECONDS, SECONDS_SLOT);

        LinkedList[] minutesSlot = new LinkedList[MINUTES_SLOT];
        wheels.put(ChronoUnit.MINUTES, minutesSlot);
        counters.put(ChronoUnit.MINUTES, currentMinute);
        slots.put(ChronoUnit.MINUTES, MINUTES_SLOT);

        LinkedList[] hoursSlot = new LinkedList[HOURS_SLOT];
        wheels.put(ChronoUnit.HOURS, hoursSlot);
        counters.put(ChronoUnit.HOURS, currentHour);
        slots.put(ChronoUnit.HOURS, HOURS_SLOT);

        LinkedList[] daysSlot = new LinkedList[DAYS_SLOT];
        wheels.put(ChronoUnit.DAYS, daysSlot);
        counters.put(ChronoUnit.DAYS, currentDay);
        slots.put(ChronoUnit.DAYS, DAYS_SLOT);
    }

    public TimeWheel() {
    }

    public TimeWheel(long maxTimeout, boolean needExitVM, boolean debugMode) {
        this.maxTimeout = maxTimeout;
        this.needExitVM = needExitVM;
        this.debugMode = debugMode;
    }

    public void start() {
        master.execute(() -> {
            while (!stop) {
                long currentMills = System.currentTimeMillis();
                try {
                    if (!debugMode) {
                        TimeUnit.MICROSECONDS.sleep(20);
                        if (currentMills - this.lastCheckMills < 1000) {
                            continue;
                        }
                    }

                    log.debug("cache: keys={}", cacheTasks.keySet());
                    if (cacheTasks.isEmpty()) {
                        if (startEmptyTime == -1) {
                            startEmptyTime = currentMills;
                        } else if (currentMills - startEmptyTime > maxTimeout) {
                            log.warn("not exist task wait and idle timeout");
                            shutdown();
                            continue;
                        }
                    }

                    this.lastCheckMills = currentMills;
                    int seconds = this.currentSecond.incrementAndGet();
                    this.pushTimeWheel(seconds);

                    LinkedList<TaskNode>[] lists = wheels.get(ChronoUnit.SECONDS);
                    LinkedList<TaskNode> list = lists[seconds % SECONDS_SLOT];
                    this.invokeAll(seconds, list);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        });
    }

    private void removeAndAdd(ChronoUnit unit, int index) {
        LinkedList<TaskNode>[] lists = wheels.get(unit);
        LinkedList<TaskNode> list = lists[index];
        if (Objects.nonNull(list) && !list.isEmpty()) {
            List<TaskNode> nodes = list.toList();
            list.clear();
            for (TaskNode node : nodes) {
                long millis = node.getLastTime() + node.getDelayMills() - System.currentTimeMillis();
                if (debugMode) {
                    millis -= SECONDS_SLOT * 1000;
                }
                if (millis < 0) {
                    log.error("system invoke task delay: delay={} node={}", millis, node);
                }

                Duration delay = Duration.ofMillis(millis);
                log.debug(": delay={}", delay);
                insertWheel(node.getId(), delay);
            }
        }
    }

    // TODO BUG
    private void pushTimeWheel(int seconds) {
        Map<ChronoUnit, Integer> tempIndex = new HashMap<>(sortedSlots.size());
        for (int i = 0; i < sortedSlots.size(); i++) {
            ChronoUnit unit = sortedSlots.get(i);
            Integer threshold = slots.get(unit);
            int upIndex = -1;

            // 最小轮
            if (i == 0) {
                if (seconds >= threshold) {
                    int up = seconds / threshold;
                    AtomicInteger counter = counters.get(unit);
                    counter.set(0);
                    upIndex = counters.get(sortedSlots.get(i + 1)).addAndGet(up);
                } else {
                    break;
                }
                // 最大轮
            } else if (i == sortedSlots.size() - 1) {
                AtomicInteger counter = counters.get(unit);
                counter.set(0);
                this.removeAndAdd(unit, 0);
                upIndex = -1;
            } else {
                AtomicInteger counter = counters.get(unit);
                if (counter.get() >= threshold) {
                    counter.set(0);
                    upIndex = counters.get(sortedSlots.get(i + 1)).incrementAndGet();
                }
            }

            if (upIndex != -1) {
                tempIndex.put(sortedSlots.get(i + 1), upIndex);
            }
        }
//    if (!tempIndex.isEmpty()) {
//      log.info(": tempIndex={}", tempIndex);
//    }

        if (!tempIndex.isEmpty()) {
            for (Entry<ChronoUnit, Integer> entry : tempIndex.entrySet()) {
                ChronoUnit unit = entry.getKey();
                Integer value = entry.getValue();
                this.removeAndAdd(unit, value % slots.get(unit));
            }
        }
    }

    private void invokeAll(int seconds, LinkedList<TaskNode> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            log.debug("no tasks need invoke");
            return;
        }

        List<TaskNode> nodes = list.toList();
        for (TaskNode node : nodes) {
            String id = node.getId();
            Callable<?> func = cacheTasks.get(id);
            log.debug("[{}]before invoke: id={}", seconds, id);
            try {
                Future<?> result = pool.submit(func);
                log.info("[{}:{}:{}] id={} result={}", this.currentSecond.get(), this.currentMinute.get(),
                        this.currentHour.get(), id, result.get());
                cacheTasks.remove(id);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        list.clear();
        log.debug("[{}]invoke all tasks successful.", seconds);
    }

    public void blockWait() throws InterruptedException {
        this.blockWait(0);
    }

    public void blockWait(long timeout) throws InterruptedException {
        synchronized (lock) {
            lock.wait(timeout);
        }
    }

    public void shutdown() {
        this.stop = true;
        log.warn("shutdown");
        synchronized (lock) {
            lock.notify();
        }
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
        log.info("sec:{} min:{} hour:{}", this.currentSecond.get(), this.currentMinute.get(),
                this.currentHour.get());
        for (ChronoUnit unit : wheels.keySet()) {
            LinkedList[] lists = wheels.get(unit);

            boolean hasData = false;
            StringBuilder builder = new StringBuilder(unit.toString());
            for (int i = 0; i < lists.length; i++) {
                LinkedList list = lists[i];
                if (Objects.nonNull(list) && !list.isEmpty()) {
                    hasData = true;
                    builder.append(String.format("[%2s] ids=%s. ", i, list.toSimpleString()));
                }
            }

            if (hasData) {
                log.info(builder.toString());
            }
        }
    }

    private boolean insertWheel(String id, Duration delay) {
        long days = delay.toDays();
        if (days > 30) {
            log.warn("out of timeWheel max delay bound");
            return false;
        }

        long current = System.currentTimeMillis();
        long delayMills = delay.toMillis();
        TaskNode node = new TaskNode(id, null, current, delayMills);
        if (days > 0) {
            return insertWheel(ChronoUnit.DAYS, this.currentDay.get() + days, node);
        }

        long hours = delay.toHours();
        if (hours > 0) {
            return insertWheel(ChronoUnit.HOURS, this.currentHour.get() + hours, node);
        }

        long minutes = delay.toMinutes();
        if (minutes > 0) {
            return insertWheel(ChronoUnit.MINUTES, this.currentMinute.get() + minutes, node);
        }

        long seconds = delay.getSeconds();
        // TODO expire too long time
        if (seconds >= -10 && seconds <= 1) {
            return insertWheel(ChronoUnit.SECONDS, this.currentSecond.get() + 1, node);
        }
        if (seconds > 1) {
            return insertWheel(ChronoUnit.SECONDS, this.currentSecond.get() + seconds, node);
        }

        log.warn("task is expire: id={} delay={}", id, delay);
        return false;
    }

    private boolean insertWheel(ChronoUnit unit, long index, TaskNode node) {
        Integer slot = slots.get(unit);
        int idx = (int) index % slot;

        LinkedList<TaskNode>[] lists = wheels.get(unit);
        LinkedList<TaskNode> list = lists[idx];
        if (Objects.isNull(list)) {
            list = new LinkedList<>();
            lists[idx] = list;
        }
        return list.add(node);
    }
}