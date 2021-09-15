package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.util.AbstractLoopEvent;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 最起初的坦克类
 * 往后有继承
 */
@Slf4j
public abstract class Tank extends AbstractLoopEvent {
    int x;          // 坦克中心的横坐标
    int y;          // 坦克中心的纵坐标
    int direct = 0;   // 初始方向
    int type = 0;     // 坦克的种类
    int speed;      // 前进的步长
    boolean alive = true;// 是否存活
    boolean abort = false; // 重开一局等外部终止因素
    int life = 1;       //生命值

    int halfWidth = 10;
    int halfHeight = 15;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void addLife(int delta) {
        this.life += delta;
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

    /**
     * 画出坦克的函数 XY是坦克中心的坐标，不是画图参照点
     */
    public void drawSelf(Graphics g) {
        //系统画图函数的参照点 （全是取的左上角）
        int topX, topY;

        switch (direct) {
            case DirectType.UP: {
                topX = this.x - this.halfWidth;
                topY = this.y - this.halfHeight;

                this.drawVerBorder(g, topX, topY);
                //5.画出炮管
                g.fill3DRect(topX + this.halfWidth - 1, topY - 1, 2, this.halfWidth + 1, false);
                break;
            }
            case DirectType.DOWN: {
                topX = this.x - this.halfWidth;
                topY = this.y - this.halfHeight;

                this.drawVerBorder(g, topX, topY);
                //5.画出炮管
                g.fill3DRect(topX + this.halfWidth - 1, topY + this.halfWidth * 2 + 1, 2, this.halfWidth + 1, false);
                break;
            }
            case DirectType.LEFT: {
                topX = this.x - this.halfHeight;
                topY = this.y - this.halfWidth;

                this.drawHorBorder(g, topX, topY);

                //5.画出炮管
                g.fill3DRect(topX - 1, topY + this.halfWidth - 1, this.halfWidth + 1, 2, false);
                break;
            }
            case DirectType.RIGHT: {
                topX = this.x - this.halfHeight;
                topY = this.y - this.halfWidth;

                this.drawHorBorder(g, topX, topY);

                //5.画出炮管
                g.fill3DRect(topX + this.halfHeight + 2, topY + this.halfWidth - 1, this.halfWidth + 1, 2, false);
                break;
            }
        }
    }

    /**
     * 水平方向
     */
    private void drawHorBorder(Graphics g, int topX, int topY) {
        final int quarterWidth = this.halfWidth / 2;
        final int tap = this.halfHeight * 2 / 7;

        for (int i = 0; i < 7; i++) {
            g.fill3DRect(topX + tap * i + 1, topY, tap - 1, quarterWidth, false);
        }

        g.fill3DRect(topX + quarterWidth * 2, topY + quarterWidth, this.halfHeight * 2 - 4 * quarterWidth, this.halfWidth, false);

        for (int i = 0; i < 7; i++) {
            g.fill3DRect(topX + tap * i + 1, topY + this.halfHeight, tap - 1, quarterWidth, false);
        }
    }

    /**
     * 竖直方向
     */
    private void drawVerBorder(Graphics g, int topX, int topY) {
        final int quarterWidth = this.halfWidth / 2;
        //1.左边的矩形
        final int tap = this.halfHeight * 2 / 7;
        for (int i = 0; i < 7; i++) {
            g.fill3DRect(topX, topY + tap * i + 1, quarterWidth, tap - 1, false);
        }

        //2.画出右边矩形
        for (int i = 0; i < 7; i++) {
            g.fill3DRect(topX + quarterWidth * 3, topY + tap * i + 1, quarterWidth, tap - 1, false);
        }

        //3.画出中间矩形
        g.fill3DRect(topX + quarterWidth, topY + quarterWidth * 2, this.halfWidth, this.halfHeight * 2 - 4 * quarterWidth, false);
    }
}
