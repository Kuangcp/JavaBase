package com.github.kuangcp.tank.util.executor;

import java.util.concurrent.Delayed;

/**
 * @author https://github.com/kuangcp on 2021-09-16 01:21
 */
public interface LoopEvent extends Runnable, Delayed {

    void addDelay(long delay);

    boolean addFixedDelay();

    /**
     * 事件需要被移除
     */
    boolean isStop();

    boolean isContinue();

    void stop();
}
