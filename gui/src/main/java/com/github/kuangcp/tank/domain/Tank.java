package com.github.kuangcp.tank.domain;

import java.awt.*;

/**
 * 最起初的坦克类
 * 往后有继承
 */
public abstract class Tank {
    int x;          // 坦克中心的横坐标
    int y;          // 坦克中心的纵坐标
    int direct = 0;   // 初始方向
    int type = 0;     // 坦克的种类
    int speed = 5;      // 前进的步长
    boolean alive = true;// 是否存活
    boolean abort = false; // 重开一局等外部终止因素
    int life = 1;//生命值

    int halfWidth = 10;
    int halfHeight = 15;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isAbort() {
        return abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean isLive) {
        this.alive = isLive;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int s) {
        speed = s;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getHalfWidth() {
        return halfWidth;
    }

    public void setHalfWidth(int halfWidth) {
        this.halfWidth = halfWidth;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(int halfHeight) {
        this.halfHeight = halfHeight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Tank(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public abstract void drawSelf(Graphics g);
}
