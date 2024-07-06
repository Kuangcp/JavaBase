package com.github.kuangcp.tank.domain;

import lombok.Getter;

/**
 * @author https://github.com/kuangcp on 2021-09-21 23:35
 */
@Getter
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
}
