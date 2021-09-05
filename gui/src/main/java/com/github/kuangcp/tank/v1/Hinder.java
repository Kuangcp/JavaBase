package com.github.kuangcp.tank.v1;

/**
 * 障碍物的最基本类
 */
public abstract class Hinder {
    int hx, hy;//障碍物坐标  绘图坐标  也就是左上角的坐标
    boolean Live;//生存状态

    public Hinder(int hx, int hy) {
        Live = true;
        this.hx = hx;
        this.hy = hy;

    }

    public Hinder() {
    }

    public Boolean getLive() {
        return Live;
    }

    public void setLive(Boolean live) {
        Live = live;
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
