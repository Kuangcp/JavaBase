package com.github.kuangcp.tank.v3;

/**
 * @author https://github.com/kuangcp on 2021-09-21 23:35
 */

public class StageBorder {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

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

    public int getTotalX() {
        return minX + maxX;
    }

    public int getTotalY() {
        return minY + maxY;
    }
}
