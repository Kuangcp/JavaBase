package thread.pool;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Kuangcp
 * 2025-05-13 14:05
 */
public class CusBenchPool extends ThreadPoolExecutor {
    private final static ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();

    private final static AtomicInteger cnt = new AtomicInteger();

    @Data
    public static class Task {
        private long startAt;
        private long endAt;
        private String name;

        public Task(String name) {
            this.name = name;
            this.startAt = System.currentTimeMillis();
        }

        public void end() {
            this.endAt = System.currentTimeMillis();
        }

        public boolean isEnd() {
            return endAt > 0;
        }

        public String toString() {
            return name + ": " + startAt + " " + endAt;
        }
    }

    @Data
    @AllArgsConstructor
    public static class ReportVO {
        private int core;
        private long cnt;
        private long total;
        private long min;
        private long max;
        private long avg;
        private long p30;
        private long p50;
        private long p90;
        private long p99;

        public String toString() {
            return "cnt: " + cnt + " total:" + total + " avg:" + avg + " min:" + min + " max:" + max
                    + " P: " + p30 + ", " + p50 + ", " + p90 + ", " + p99;
        }

        public String format() {
            return String.format("core:%2d cnt:%4d total:%8d avg:%5d min:%5d max:%5d P: %5d,%5d,%5d,%5d",
                    core, cnt, total, avg, min, max, p30, p50, p90, p99);
        }
    }

    public CusBenchPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                        TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CusBenchPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        if (Objects.isNull(command)) {
            return;
        }
        super.execute(() -> {
            Task task = new Task(cnt.incrementAndGet() + "");
            queue.add(task);

            command.run();
            task.end();
        });
    }

    public String showTask() {
        StringBuilder res = new StringBuilder();
        for (Task task : queue) {
            res.append(task.toString()).append("\n");
        }
        return res.toString();
    }

    public ReportVO statisticsTask() {
        long total = 0;
        long cnt = 0;
        List<Long> all = new ArrayList<>();
        for (Task task : queue) {
            if (!task.isEnd()) {
                continue;
            }
            cnt++;
            long run = task.endAt - task.startAt;
            total += run;
            all.add(run);
        }
        List<Long> sorted = all.stream().sorted().collect(Collectors.toList());
        int size = sorted.size();
        Long P30 = sorted.get((int) (size * 0.3));
        Long P50 = sorted.get((int) (size * 0.5));
        Long P90 = sorted.get((int) (size * 0.9));
        Long P99 = sorted.get((int) (size * 0.99));

        return new ReportVO(getCorePoolSize(), cnt, total, sorted.get(0), sorted.get(size - 1), total / cnt, P30, P50, P90, P99);
    }
}
