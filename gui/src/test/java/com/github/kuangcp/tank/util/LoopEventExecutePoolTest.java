package com.github.kuangcp.tank.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-16 01:28
 */
@Slf4j
public class LoopEventExecutePoolTest {

    static class SampleTask implements Delayed {
        String id;
        long time;

        public SampleTask(String id, long time) {
            this.time = time;
            this.id = id;
        }

        public void addDelay(long delay) {
            this.time += delay;
        }

        public long getTime() {
            return time;
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "SampleTask{" +
                    "id='" + id + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    static class EventTask extends AbstractLoopEvent {
        public int sx;
        public int sy;
        public int direct = 0;
        public static int speed = 3;//如果改动要记得按钮事件里也要改

        public EventTask(String id, long fixedDelay, long delayStart, TimeUnit timeUnit, int x, int y) {
            super(id, fixedDelay, delayStart, timeUnit);
            this.sx = x;
            this.sy = y;
        }

        @Override
        public void run() {
            // 每个子弹发射的延迟运动的时间
//            TankTool.yieldMsTime(55);

            switch (direct) {
                //上下左右
                case 0:
                    sy -= speed;
                    break;
                case 1:
                    sy += speed;
                    break;
                case 2:
                    sx -= speed;
                    break;
                case 3:
                    sx += speed;
                    break;
            }

            //判断子弹是否碰到边缘
            if (sx < 20 || sx > 740 || sy < 20 || sy > 540) {
                this.stop();
            }

            if (sx < 440 && sx > 380 && sy < 540 && sy > 480) {
                this.stop();
            }
            log.info("sx={} sy={}", sx, sy);
        }
    }

    @Test
    public void testAction() throws Exception {
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(100000) + 1000000; i++) {
            Objects.hash(UUID.randomUUID());
        }
    }

    @Test
    public void testRun() throws Exception {
        BlockingQueue<SampleTask> delayQueue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        delayQueue.put(new SampleTask("A", now + 1000));
        delayQueue.put(new SampleTask("B", now + 1500));
        delayQueue.put(new SampleTask("C", now + 1800));

        while (true) {
            final SampleTask tmp = delayQueue.take();
            now = System.currentTimeMillis();
            log.info("task={} delay={}", tmp, now - tmp.getTime());

            for (int i = 0; i < ThreadLocalRandom.current().nextInt(100000) + 1000000; i++) {
                Objects.hash(UUID.randomUUID());
            }

            tmp.addDelay(1000);
            delayQueue.add(tmp);
        }
    }

    @Test
    public void testShotBullet() throws Exception {
        BlockingQueue<EventTask> delayQueue = new DelayQueue<>();
        delayQueue.add(new EventTask("xx", 55, 55, TimeUnit.MILLISECONDS, 60, 120));
        while (true) {
            final EventTask task = delayQueue.take();
            task.run();
            if (task.isContinue()) {
                task.addFixedDelay();
                delayQueue.add(task);
            }
        }
    }

    @Test
    public void testTime() throws Exception {
        System.out.println(TimeUnit.SECONDS.toMillis(20));
    }

}