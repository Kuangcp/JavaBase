package com.github.kuangcp.tank.domain;

import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-21 23:35
 */
@Data
public class StageBorder {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    private int homeX;
    private int homeY;
    private int homeW;
    private int homeH;

    public StageBorder(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getTotalX() {
        return minX + maxX;
    }

    public int getTotalY() {
        return minY + maxY;
    }

    public boolean hitHome(int x, int y) {
        return x < homeX + homeW && x > homeX && y < homeY + homeH && y > homeY;
    }
}
