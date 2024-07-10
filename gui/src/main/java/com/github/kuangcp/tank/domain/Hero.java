package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.resource.ColorMgr;
import com.github.kuangcp.tank.util.HoldingKeyStateMgr;
import com.github.kuangcp.tank.util.Roll;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Vector;

@Slf4j
@Getter
@Setter
public class Hero extends Tank {

    //子弹集合
    public Vector<Bullet> bulletList = new Vector<>();
    private long lastShotTick = 0;
    private long shotCDMs = 268;

    private final int originX, originY;

    public Bullet bullet = null;//子弹
    private int prize = 0;//击敌个数
    public int maxLiveShot = 7;//主坦克子弹线程存活的最大数
    private long lastDieMs = System.currentTimeMillis();
    private int normalColor = 0;

    public Hero(int x, int y, int speed) {
        super(x, y, speed);
        this.originX = x;
        this.originY = y;
    }

    @Override
    public void run() {
        final HoldingKeyStateMgr keyEvent = HoldingKeyStateMgr.instance;

        while (this.isAlive()) {
            if (keyEvent.hasPressMoveEvent()) {
//                log.info("eventGroup={}", eventGroup);

                final int lastDirect = this.getDirect();
                final int direct = keyEvent.getDirect();

                final boolean ablePass = PlayStageMgr.ablePassByHinder(this);
                if ((ablePass || lastDirect != direct)) {
                    this.setDirect(direct);
                    if (PlayStageMgr.instance.willInBorder(this)
                            && PlayStageMgr.instance.ableToMove(this)) {
                        this.move();
                    }
                }
            }

            if (keyEvent.isShot()) {
                this.shotEnemy();
            }

            // 动作的延迟 1000 / 77 fps
            TankTool.yieldMsTime(33);
        }
    }

    /**
     * 画出坦克的函数 XY是坦克中心的坐标，不是画图参照点
     */
    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.YELLOW);
        if (this.isInvincible()) {
            g.setColor(Color.CYAN);
            // 闪烁
            normalColor++;
            normalColor %= 28;
            if (normalColor % 14 < 3) {
                g.setColor(ColorMgr.instance.bgColor);
            }
        }
        super.drawSelf(g);
    }

    public boolean isInvincible() {
        // 复活 无敌
        if (System.currentTimeMillis() - this.lastDieMs < PlayStageMgr.getInvincibleMs()) {
            return true;
        }
        // TODO 道具 无敌
        return false;
    }

    @Override
    public boolean hitByBullet() {
        // 无敌
        if (this.isInvincible()) {
            return false;
        }
        this.resurrect();
        return super.hitByBullet();
    }

    public void resurrect() {
        if (!this.alive) {
            return;
        }
        this.lastDieMs = System.currentTimeMillis();

        // 100‰ 概率原地复活
        if (Roll.roll(100)) {
            return;
        }

        this.setX(this.originX);
        this.setY(this.originY);
        this.setDirect(DirectType.UP);
    }

    public void shotEnemy() {
        final long nowMs = System.currentTimeMillis();
        if (lastShotTick != 0 && nowMs - lastShotTick < shotCDMs) {
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
//        shotExecutePool.execute(bullet);
        LoopEventExecutor.addLoopEvent(bullet);
        lastShotTick = nowMs;
    }

    public void addSpeed(int delta) {
        this.speed += delta;
    }

    public void addPrize(int delta) {
        this.prize += delta;
    }


    @Override
    public String toString() {
        return "Hero{" +
                "bulletList=" + bulletList +
                ", lastShotTick=" + lastShotTick +
                ", shotCDMs=" + shotCDMs +
                ", x=" + x +
                ", y=" + y +
                ", prize=" + prize +
                ", maxLiveShot=" + maxLiveShot +
                ", speed=" + speed +
                '}';
    }
}
