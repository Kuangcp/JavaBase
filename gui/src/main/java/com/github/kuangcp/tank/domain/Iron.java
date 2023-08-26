package com.github.kuangcp.tank.domain;

/**
 * 铁块
 */
public class Iron extends Hinder {
    public Iron(int i, int j) {
        super(i, j);
    }

    @Override
    public int getWidth() {
        return 10;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
