package com.github.kuangcp.tank.domain;


import com.github.kuangcp.tank.util.ExecutePool;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class Hero extends Tank {

    //子弹集合
    public Vector<Shot> shotList = new Vector<>();
    private final ExecutorService shotExecutePool;
    private long lastShotMs = 0;
    private long shotCDMs = 268;

    public Shot shot = null;//子弹
    public Graphics g; //画笔不可少
    private int prize = 0;//击敌个数
    public int maxLiveShot = 8;//主坦克子弹线程存活的最大数
    int speed = 3;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addSpeed(int delta) {
        this.speed += delta;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public void addPrize(int delta) {
        this.prize += delta;
    }


    public Hero(int x, int y, int speed) {
        super(x, y, speed);
        this.shotExecutePool = ExecutePool.buildFixedPool("heroShot", maxLiveShot);
    }

    public void shotEnemy() {
        final long nowMs = System.currentTimeMillis();
        if (lastShotMs != 0 && nowMs - lastShotMs < shotCDMs) {
            return;
        }
        if (this.shotList.size() >= this.maxLiveShot || !this.isAlive()) {
            return;
        }

        //判断坦克方向来 初始化子弹的起始发射位置
        switch (this.getDirect()) {
            case 0://0123 代表 上下左右
                shot = new Shot(this.getX() - 1, this.getY() - 15, 0);
                shotList.add(shot);
                break;
            case 1:
                shot = new Shot(this.getX() - 2, this.getY() + 15, 1);
                shotList.add(shot);
                break;
            case 2:
                shot = new Shot(this.getX() - 15 - 2, this.getY(), 2);
                shotList.add(shot);
                break;
            case 3:
                shot = new Shot(this.getX() + 15 - 2, this.getY() - 1, 3);
                shotList.add(shot);
                break;
        }
        //启动子弹线程
        shotExecutePool.execute(shot);
        lastShotMs = nowMs;
    }

    public void moveUp() {
        setY(getY() - getSpeed());
    }

    public void moveDown() {
        setY(getY() + getSpeed());
    }

    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    public void moveRight() {
        setX(getX() + getSpeed());
    }
}
