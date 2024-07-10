package com.github.kuangcp.tank.util.executor;

import com.github.kuangcp.tank.domain.AnyLife;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-16 01:44
 */
@Slf4j
public abstract class AbstractLoopEvent extends AnyLife implements LoopEvent {

    private static final long defaultDelay = 20;
    /**
     * 时间id
     */
    public String id;
    /**
     * 下次事件触发时机
     */
    long nextTickTime;

    long fixedDelayTime = 40;

    Runnable stopHook;

    public AbstractLoopEvent() {
        this.id = UUID.randomUUID().toString();
        this.nextTickTime = System.currentTimeMillis() + defaultDelay;
    }

    // 构建任务的补充参数
    public void setId(String id) {
        this.id = id;
    }

    public void setFixedDelayTime(long fixedDelayTime) {
        this.fixedDelayTime = fixedDelayTime;
    }

    public void setDelayStart(long delayStart, TimeUnit timeUnit) {
        this.nextTickTime += timeUnit.toMillis(delayStart);
    }

    // 业务动作
    @Override
    public void addDelay(long delay) {
        this.nextTickTime += delay;
    }

    @Override
    public boolean addFixedDelay() {
        if (fixedDelayTime <= 0) {
            return false;
        }
        this.addDelay(fixedDelayTime);
        return isContinue();
    }

    @Override
    public boolean isStop() {
        return this.nextTickTime <= 0;
    }

    @Override
    public boolean isContinue() {
        return !isStop();
    }

    @Override
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
