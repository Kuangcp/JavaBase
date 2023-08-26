package com.github.kuangcp.tank.domain;

/**
 * 砖块
 */
public class Brick extends Hinder {

    public Brick(int hx, int hy) {
        super(hx, hy);
    }

    @Override
    public int getWidth() {
        return 7;
    }

    @Override
    public int getHeight() {
        return 7;
    }
}
