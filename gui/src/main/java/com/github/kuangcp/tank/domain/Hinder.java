package com.github.kuangcp.tank.domain;

/**
 * 障碍物的最基本类
 */
public abstract class Hinder {

    int hx, hy;//障碍物绘图坐标  左上角顶点 的坐标
    boolean alive;//存活状态

    public Hinder(int hx, int hy) {
        alive = true;
        this.hx = hx;
        this.hy = hy;
    }

    public Hinder() {
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public int getHx() {
        return hx;
    }

    public void setHx(int hx) {
        this.hx = hx;
    }

    public int getHy() {
        return hy;
    }

    public void setHy(int hy) {
        this.hy = hy;
    }


}
