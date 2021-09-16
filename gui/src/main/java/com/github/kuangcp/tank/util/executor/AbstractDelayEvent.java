package com.github.kuangcp.tank.util.executor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-17 01:39
 */
public abstract class AbstractDelayEvent implements DelayEvent {

    /**
     * 下次事件触发时机
     */
    long delayTime;

    public AbstractDelayEvent(long delayTime) {
        this.delayTime = delayTime;
    }


    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
}
