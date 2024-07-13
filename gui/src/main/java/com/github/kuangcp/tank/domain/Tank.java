package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最起初的坦克类
 * 往后有继承
 */
@Setter
@Getter
@Slf4j
public abstract class Tank extends MoveLoopEvent implements VisualItem {
    private static final AtomicInteger idCnt = new AtomicInteger(0);
    int id;

    int type = 0;     // 坦克的种类
    boolean abort = false; // 重开一局等外部终止因素
    int life = 1;       //生命值

    int halfWidth = 10;
    int halfHeight = 15;
    private final int wheelNum = 7;
    public List<Bullet> bulletList = new LinkedList<>();


    public Tank(int x, int y, int speed) {
        this.id = idCnt.incrementAndGet();
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.alive = true;
    }

    public void addLife(int delta) {
        this.life += delta;
        if (this.life <= 0) {
            alive = false;
        }
    }

    @Override
    public void run() {

    }

    public void shotBullet() {
        switch (this.direct) {
            case DirectType.UP: {
                this.shot(this.x - 1, this.y - 15, DirectType.UP);
                break;
            }
            case DirectType.DOWN: {
                this.shot(this.x - 2, this.y + 15, DirectType.DOWN);
                break;
            }
            case DirectType.LEFT: {
                this.shot(this.x - 15 - 2, this.y, DirectType.LEFT);
                break;
            }
            case DirectType.RIGHT: {
                this.shot(this.x + 15 - 2, this.y - 1, DirectType.RIGHT);
                break;
            }
        }
    }

    private void shot(int x, int y, int direction) {
        Bullet s = new Bullet(x, y, direction);
        bulletList.add(s);
        LoopEventExecutor.addLoopEvent(s);
    }

    /**
     * @return draw bomb
     */
    public boolean hitByBullet() {
        this.addLife(-1);
        return true;
    }


    /**
     * 画出坦克的函数 XY是坦克中心的坐标，不是画图参照点
     */
    public void drawSelf(Graphics g) {
        //系统画图函数的参照点 （全是取的左上角）
        int topX, topY;
        switch (direct) {
            case DirectType.UP: {
                drawUp(g);
                break;
            }
            case DirectType.DOWN: {
                topX = this.x - this.halfWidth;
                topY = this.y - this.halfHeight;

                this.drawVerBorder(g, topX, topY);
                g.fill3DRect(topX + this.halfWidth - 1, topY + this.halfWidth * 2 + 1, 2, this.halfWidth + 1, false);
                break;
            }
            case DirectType.LEFT: {
                topX = this.x - this.halfHeight;
                topY = this.y - this.halfWidth;

                this.drawHorBorder(g, topX, topY);

                g.fill3DRect(topX - 1, topY + this.halfWidth - 1, this.halfWidth + 1, 2, false);
                break;
            }
            case DirectType.RIGHT: {
                topX = this.x - this.halfHeight;
                topY = this.y - this.halfWidth;

                this.drawHorBorder(g, topX, topY);

                g.fill3DRect(topX + this.halfHeight + 2, topY + this.halfWidth - 1, this.halfWidth + 1, 2, false);
                break;
            }
            default:
                drawUp(g);
        }
    }

    private void drawUp(Graphics g) {
        int topX = this.x - this.halfWidth;
        int topY = this.y - this.halfHeight;

        this.drawVerBorder(g, topX, topY);
        g.fill3DRect(topX + this.halfWidth - 1, topY - 1, 2, this.halfWidth + 1, false);
    }

    /**
     * 水平方向
     */
    private void drawHorBorder(Graphics g, int topX, int topY) {
        final int quarterWidth = this.halfWidth / 2;
        final int tap = this.halfHeight * 2 / wheelNum;

        for (int i = 0; i < wheelNum; i++) {
            g.fill3DRect(topX + tap * i + 1, topY, tap - 1, quarterWidth, false);
        }

        g.fill3DRect(topX + quarterWidth * 2, topY + quarterWidth, this.halfHeight * 2 - 4 * quarterWidth, this.halfWidth, false);

        for (int i = 0; i < wheelNum; i++) {
            g.fill3DRect(topX + tap * i + 1, topY + this.halfHeight, tap - 1, quarterWidth, false);
        }
    }

    /**
     * 竖直方向
     */
    private void drawVerBorder(Graphics g, int topX, int topY) {
        final int quarterWidth = this.halfWidth / 2;
        //1.左边的矩形
        final int tap = this.halfHeight * 2 / wheelNum;
        for (int i = 0; i < wheelNum; i++) {
            g.fill3DRect(topX, topY + tap * i + 1, quarterWidth, tap - 1, false);
        }

        //2.画出右边矩形
        for (int i = 0; i < wheelNum; i++) {
            g.fill3DRect(topX + quarterWidth * 3, topY + tap * i + 1, quarterWidth, tap - 1, false);
        }

        //3.画出中间矩形
        g.fill3DRect(topX + quarterWidth, topY + quarterWidth * 2, this.halfWidth, this.halfHeight * 2 - 4 * quarterWidth, false);
    }
}
