package com.github.kuangcp.tank.domain;


import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.util.ExecutePool;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class Hero extends Tank {

    //子弹集合
    public Vector<Bullet> bulletList = new Vector<>();
    private final ExecutorService shotExecutePool;
    private long lastShotMs = 0;
    private long shotCDMs = 268;

    private int originX, originY;

    public Bullet bullet = null;//子弹
    private int prize = 0;//击敌个数
    public int maxLiveShot = 7;//主坦克子弹线程存活的最大数

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
        this.originX = x;
        this.originY = y;
        this.shotExecutePool = ExecutePool.buildFixedPool("heroShot", maxLiveShot);
    }

    public void resurrect() {
        this.setX(this.originX);
        this.setY(this.originY);
        this.setDirect(DirectType.UP);
    }

    public void shotEnemy() {
        final long nowMs = System.currentTimeMillis();
        if (lastShotMs != 0 && nowMs - lastShotMs < shotCDMs) {
            return;
        }
        if (this.bulletList.size() >= this.maxLiveShot || !this.isAlive()) {
            return;
        }

        //判断坦克方向来 初始化子弹的起始发射位置
        switch (this.getDirect()) {
            case 0://0123 代表 上下左右
                bullet = new Bullet(this.getX() - 1, this.getY() - 15, 0);
                bulletList.add(bullet);
                break;
            case 1:
                bullet = new Bullet(this.getX() - 2, this.getY() + 15, 1);
                bulletList.add(bullet);
                break;
            case 2:
                bullet = new Bullet(this.getX() - 15 - 2, this.getY(), 2);
                bulletList.add(bullet);
                break;
            case 3:
                bullet = new Bullet(this.getX() + 15 - 2, this.getY() - 1, 3);
                bulletList.add(bullet);
                break;
        }
        //启动子弹线程
        shotExecutePool.execute(bullet);
        lastShotMs = nowMs;
    }

    public void moveUp() {
        this.y -= this.speed;
    }

    public void moveDown() {
        this.y += this.speed;
    }

    public void moveLeft() {
        this.x -= this.speed;
    }

    public void moveRight() {
        this.x += this.speed;
    }

    /**
     * 画出坦克的函数 XY是坦克中心的坐标，不是画图参照点
     */
    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.yellow);
        super.drawSelf(g);
    }


    @Override
    public String toString() {
        return "Hero{" +
                "bulletList=" + bulletList +
                ", lastShotMs=" + lastShotMs +
                ", shotCDMs=" + shotCDMs +
                ", x=" + x +
                ", y=" + y +
                ", prize=" + prize +
                ", maxLiveShot=" + maxLiveShot +
                ", speed=" + speed +
                '}';
    }
}
