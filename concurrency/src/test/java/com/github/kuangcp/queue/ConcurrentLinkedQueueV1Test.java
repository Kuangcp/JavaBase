package com.github.kuangcp.queue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Kuangcp
 * 2026-01-07 14:17
 */
@Slf4j
public class ConcurrentLinkedQueueV1Test {

    private final ThreadLocal<Long> cnt = new ThreadLocal<>();
    private final ThreadLocal<Long> id = new ThreadLocal<>();

    private final ConcurrentLinkedQueue<String> msgQueue = new ConcurrentLinkedQueue<>();
    // 窗口的最大容量
    private final static int boomThreshold = 200;
    private final AtomicLong lastAlertTime = new AtomicLong(0);
    // 时间窗口流攒批
    private static final Duration BATCH_INTERVAL = Duration.ofSeconds(10);
    private static final Duration MIN_INTERVAL = Duration.ofSeconds(10);

    /**
     * 定时批量发送任务的Future，用于取消任务
     */
    private volatile ScheduledFuture<?> scheduledTask;

    static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("batch")
                    .daemon(true).build());

    public void init() {
        // 启动定时批量处理任务
        scheduledTask = scheduler.scheduleAtFixedRate(
                this::batchSend,
                BATCH_INTERVAL.toMillis(),
                BATCH_INTERVAL.toMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public void destroy() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        // 关闭前发送剩余消息
        batchSend();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void verify(String block) {
        String[] all = block.split(",");
        Map<Long, List<Pair<Long, String>>> threadMap = Stream.of(all).map(v -> {
            String[] sl = v.split("_");
            return Pair.create(Long.parseLong(sl[0]), sl[1]);
        }).collect(Collectors.groupingBy(Pair::getKey));
        List<Long> sortKey = threadMap.keySet().stream().sorted().collect(Collectors.toList());
        StringBuilder buf = new StringBuilder();
        for (Long key : sortKey) {
            List<Pair<Long, String>> now = threadMap.get(key);
            List<Long> vals = now.stream().map(Pair::getValue).map(Long::parseLong).sorted().collect(Collectors.toList());
            Long min = vals.get(0);
            Long max = vals.get(vals.size() - 1);
            buf.append(String.format("%s [%3d,%3d] ", key, min, max));
        }
        log.info("{}", buf);
    }

    private void consumer(String msg) {
        // 但是这里会占用生产者 线程资源
//        long now = System.currentTimeMillis();
//        long last = lastAlertTime.get();
//        long elapsed = now - last;

        // 如果距离上次发送超过最小间隔，立即发送
//        if (elapsed >= MIN_INTERVAL.toMillis()) {
//            if (lastAlertTime.compareAndSet(last, now)) {
//                batchSend();
//                return;
//            }
//        }

        // 否则加入队列等待批量处理
        // 先检查队列大小，避免内存溢出
        if (msgQueue.size() >= boomThreshold) {
            return;
        }

        msgQueue.offer(msg);
    }

    private void batchSend() {
        if (msgQueue.isEmpty()) {
            return;
        }

        // 批量取出队列中的所有消息
        List<String> messages = new ArrayList<>();
        String tmp;
        while ((tmp = msgQueue.poll()) != null) {
            messages.add(tmp);
        }
        if (!messages.isEmpty()) {
            String msgBlock = String.join(",", messages);
            lastAlertTime.set(System.currentTimeMillis());
            verify(msgBlock);
        }
    }

    private void submitTask(int i) {
        cnt.set(0L);
        id.set((long) i);
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100 + ThreadLocalRandom.current().nextInt(800));
                Long val = cnt.get();
                long start = System.currentTimeMillis();
                consumer(id.get() + "_" + val);
                long ms = System.currentTimeMillis() - start;
                if (ms > 1) {
                    log.info("ms={}", ms);
                }
                cnt.set(val + 1);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 流攒批 处理
     */
    @Test
    public void testWaitBatch() throws Exception {
        init();
        int num = 30;
        ExecutorService producer = Executors.newFixedThreadPool(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            producer.submit(() -> this.submitTask(finalI + 1));
        }

        Thread.currentThread().join();
    }
}
