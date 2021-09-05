package com.github.kuangcp.tank.v1;


import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.v2.Shot;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hero extends Tank {

    //子弹集合
    public Vector<Shot> ss = new Vector<>();
    private final ExecutorService shotExecutePool;

    public Shot s = null;//子弹
    public Graphics g; //画笔不可少
    public boolean flag = false;//是否击中
    static int round = 0;
    public Vector<Demons> ets;//只是一个指针（引用）
    public Vector<Brick> bricks;
    public Vector<Iron> irons;
    public boolean to = true;
    static int prize = 0;//击敌个数
    public int maxLiveShot = 8;//主坦克子弹线程存活的最大数
    int speed = 3;
    static int Life = 10;


    public int getLife() {
        return Life;
    }

    public void setLife(int life) {
        Life = life;
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


    public Hero(int x, int y, int speed, Vector<Demons> ets, Vector<Brick> bricks, Vector<Iron> irons) {
        super(x, y, speed);
//		this.Life = 10;
//		要给g分配内存 初始化对象
//		g = new Graphics();
//		g.setColor(Color.yellow);
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;

        this.shotExecutePool = Executors.newFixedThreadPool(maxLiveShot);
    }

    public void shotEnemy() {
        if (this.ss.size() >= this.maxLiveShot || !this.getisLive()) {
            return;
        }

        //判断坦克方向来 初始化子弹的起始发射位置
        switch (this.getDirect()) {
            case 0://0123 代表 上下左右
                s = new Shot(this.getX() - 1, this.getY() - 15, 0);
                ss.add(s);
                break;
            case 1:
                s = new Shot(this.getX() - 2, this.getY() + 15, 1);
                ss.add(s);
                break;
            case 2:
                s = new Shot(this.getX() - 15 - 2, this.getY(), 2);
                ss.add(s);
                break;
            case 3:
                s = new Shot(this.getX() + 15 - 2, this.getY() - 1, 3);
                ss.add(s);
                break;
        }
        //启动子弹线程
        shotExecutePool.execute(s);
    }

    //移动的函数
    public void moveup() {
        to = true;
        //检测敌方坦克碰撞
        for (Demons et : ets) {
            if (!TankTool.Rush(this, et))
                to = false;
        }
        //检测障碍物
        for (Brick brick : bricks) {
            if (TankTool.hasHint(this, brick))
                to = false;
        }
        for (Iron iron : irons) {
            if (TankTool.hasHint(this, iron))
                to = false;
        }
        if (to)
            setY(getY() - getSpeed());
    }

    public void movedown() {
        to = true;
        for (Demons et : ets)
            if (!TankTool.Rush(this, et))
                to = false;
        for (Brick brick : bricks) {
            if (TankTool.hasHint(this, brick))
                to = false;
        }
        for (Iron iron : irons) {
            if (TankTool.hasHint(this, iron))
                to = false;
        }
        if (to)
            setY(getY() + getSpeed());
    }

    public void moveleft() {
        to = true;
        for (Demons et : ets)
            if (!TankTool.Rush(this, et))
                to = false;
        for (Brick brick : bricks) {
            if (TankTool.hasHint(this, brick))
                to = false;
        }
        for (Iron iron : irons) {
            if (TankTool.hasHint(this, iron))
                to = false;
        }
        if (to)
            setX(getX() - getSpeed());
    }

    public void moveright() {
        to = true;
        for (Demons et : ets)
            if (!TankTool.Rush(this, et))
                to = false;
        for (Brick brick : bricks) {
            if (TankTool.hasHint(this, brick))
                to = false;
        }
        for (Iron iron : irons) {
            if (TankTool.hasHint(this, iron))
                to = false;
        }
        if (to)
            setX(getX() + getSpeed());
    }
}
