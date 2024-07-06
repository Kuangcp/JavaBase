package com.github.kuangcp.tank.constant;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-06 03:08
 */
public interface DirectType {

    int NONE = -1;

    int UP = 0;
    int DOWN = 1;
    int LEFT = 2;
    int RIGHT = 3;

    int MAX = 3;

    int[] UP_SELECT = new int[]{UP, LEFT, RIGHT};
    int[] DOWN_SELECT = new int[]{DOWN, LEFT, RIGHT};
    int[] LEFT_SELECT = new int[]{UP, DOWN, LEFT};
    int[] RIGHT_SELECT = new int[]{UP, DOWN, RIGHT};

    int[] loop = new int[]{UP, RIGHT, DOWN, LEFT};

    static int[] turnSelection(int direct) {
        switch (direct) {
            case UP:
                return UP_SELECT;
            case DOWN:
                return DOWN_SELECT;
            case LEFT:
                return LEFT_SELECT;
            case RIGHT:
                return RIGHT_SELECT;
            default:
                return UP_SELECT;
        }
    }

    static boolean isUp(int direct) {
        return direct == UP;
    }

    static boolean isDown(int direct) {
        return direct == DOWN;
    }

    static boolean isLeft(int direct) {
        return direct == LEFT;
    }

    static boolean isRight(int direct) {
        return direct == RIGHT;
    }

    static int rollDirect(int curDirect) {
        return DirectType.turnSelection(curDirect)[ThreadLocalRandom.current().nextInt(DirectType.MAX)];
    }
}
