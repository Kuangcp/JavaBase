package com.github.kuangcp.tank.domain.robot;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-22 23:56
 */
public class EnemyActionContext {

    public static final int MAX_DIRECT_STEP = 20;
    public static final int MIN_DIRECT_STEP = 3;

    private long lastShotTime;
    private long round;
    private int curRoundStep = 3;

    private int sameDirectCounter = 0;

    public int getSameDirectCounter() {
        return sameDirectCounter;
    }

    public long getRound() {
        return round;
    }

    public void addRound() {
        this.round += 1;
    }

    private long getAndAddCount() {
        final long origin = this.round;
        this.round += 1;
        return origin;
    }

    public RoundActionEnum roundAction(RobotRate rate) {
        final long round = this.getAndAddCount();
        if (round % rate.getMoveRate() == rate.getMoveRate() - 1) {
            return RoundActionEnum.MOVE;
        }

        if (round % rate.getShotRate() == rate.getShotRate() - 1) {
            return RoundActionEnum.SHOT;
        }

        return RoundActionEnum.STAY;
    }

    public boolean isShotRound() {
        return true;
    }

    public void addCount() {
        this.sameDirectCounter += 1;
    }

    public void resetDirectCount() {
        this.sameDirectCounter = 0;
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int curRoundStep = random.nextInt(EnemyActionContext.MIN_DIRECT_STEP, EnemyActionContext.MAX_DIRECT_STEP);
        this.setCurRoundStep(curRoundStep);
    }

    public int getCurRoundStep() {
        return curRoundStep;
    }

    public void setCurRoundStep(int curRoundStep) {
        this.curRoundStep = curRoundStep;
    }
}
