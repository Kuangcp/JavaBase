package com.github.kuangcp.tank.domain.event;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-17 01:39
 */
public abstract class DelayEvent implements Runnable, Delayed {

    /**
     * 下次事件触发时机
     */
    public long delayTime;

    public DelayEvent(long delayTime) {
        this.delayTime = delayTime + System.currentTimeMillis();
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
