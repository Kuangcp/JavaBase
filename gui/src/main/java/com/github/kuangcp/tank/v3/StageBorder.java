package com.github.kuangcp.tank.v3;

/**
 * @author https://github.com/kuangcp on 2021-09-21 23:35
 */

public class StageBorder {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public StageBorder(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}
