package com.github.kuangcp.tank.domain;

import lombok.Data;

/**
 * 障碍物的最基本类
 */
@Data
public abstract class Hinder {

    int hx, hy; // 障碍物绘图坐标  左上角顶点 的坐标
    boolean alive;//存活状态
    int width = 20;
    int height = 10;

    public Hinder(int hx, int hy) {
        alive = true;
        this.hx = hx;
        this.hy = hy;
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }
}
