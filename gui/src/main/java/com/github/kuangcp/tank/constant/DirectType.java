package com.github.kuangcp.tank.constant;

/**
 * @author https://github.com/kuangcp on 2021-09-06 03:08
 */
public interface DirectType {
    int UP = 0;
    int DOWN = 1;
    int LEFT = 2;
    int RIGHT = 3;

    int[] UP_SELECT = new int[]{UP, LEFT, RIGHT};
    int[] DOWN_SELECT = new int[]{DOWN, LEFT, RIGHT};
    int[] LEFT_SELECT = new int[]{UP, DOWN, LEFT};
    int[] RIGHT_SELECT = new int[]{UP, DOWN, RIGHT};

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
}
