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
import java.util.concurrent.atomic.AtomicBoolean;
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

    private final ConcurrentLinkedQueue<String> msgQueue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean delay = new AtomicBoolean();
    private final AtomicLong lastSend = new AtomicLong();
    private final ThreadLocal<Long> cnt = new ThreadLocal<>();
    private final ThreadLocal<Long> id = new ThreadLocal<>();

    // 窗口的最大容量
    private final static int boomThreshold = 200;
    // 时间窗口流攒批
    private final Duration window = Duration.ofSeconds(7);

    static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("batch")
                    .daemon(true).build());

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

    private void handle(String msg) {
        long now = System.currentTimeMillis();
        long last = lastSend.get();
        Duration delta = Duration.ofMillis(now - last);

        if (msgQueue.size() > boomThreshold) {
//            log.warn("BOOM");
            return;
        }
        msgQueue.offer(msg);
        if (delay.get()) {
            return;
        }

        delay.set(true);
        scheduler.schedule(() -> {
            // 批量取出队列中的所有消息
            List<String> messages = new ArrayList<>();
            String tmp;
            while ((tmp = msgQueue.poll()) != null) {
                messages.add(tmp);
            }
            if (!messages.isEmpty()) {
                String msgBlock = String.join(",", messages);
                lastSend.set(System.currentTimeMillis());
//                log.info("msgBlock={}", msgBlock);
                verify(msgBlock);
            }


            delay.set(false);
        }, window.minus(delta).toMillis(), TimeUnit.MILLISECONDS);
    }

    private void submitTask(int i) {
        cnt.set(0L);
        id.set((long) i);
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100 + ThreadLocalRandom.current().nextInt(800));
                Long val = cnt.get();
                long start = System.currentTimeMillis();
                handle(id.get() + "_" + val);
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
        int num = 30;
        ExecutorService producer = Executors.newFixedThreadPool(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            producer.submit(() -> this.submitTask(finalI + 1));
        }

        Thread.currentThread().join();
    }
}
