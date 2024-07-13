package com.github.kuangcp.tank.domain.event;

import com.github.kuangcp.tank.domain.AnyLife;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-16 01:44
 */
@Slf4j
public abstract class LoopEvent extends AnyLife implements Runnable, Delayed {

    private static final AtomicLong counter = new AtomicLong();
    private static final long defaultDelay = 20;
    // 构建任务的补充参数
    /**
     * 事件id
     */
    public Long id;
    /**
     * 下次事件触发时机
     */
    public long nextTickTime;

    @Setter
    public long fixedDelayTime = 40;

    public Runnable stopHook;

    public LoopEvent() {
        this.id = counter.incrementAndGet();
        this.nextTickTime = System.currentTimeMillis() + defaultDelay;
    }

    public void setDelayStart(long delayStart, TimeUnit timeUnit) {
        this.nextTickTime += timeUnit.toMillis(delayStart);
    }

    // 业务动作
    public void addDelay(long delay) {
        this.nextTickTime += delay;
    }

    public boolean addFixedDelay() {
        if (fixedDelayTime <= 0) {
            return false;
        }
        this.addDelay(fixedDelayTime);
        return isContinue();
    }

    public boolean isStop() {
        return this.nextTickTime <= 0;
    }

    public boolean isContinue() {
        return !isStop();
    }

    public void stop() {
//        log.info("{} stop", this);
        this.nextTickTime = 0;

        if (Objects.nonNull(this.stopHook)) {
            stopHook.run();
        }
    }

    public void registerHook(Runnable stopHook) {
        this.stopHook = stopHook;
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(nextTickTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "AbstractLoopEvent{" +
                "id='" + id + '\'' +
                ", nextTickTime=" + nextTickTime +
                ", fixedDelayTime=" + fixedDelayTime +
                '}';
    }
}
