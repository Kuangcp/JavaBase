package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.domain.event.MoveLoopEvent;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 子弹
 * <p>
 * TODO 对象池（弹夹） 复用对象
 * TODO 抽象类，多种子弹
 */
@Slf4j
public class Bullet extends MoveLoopEvent implements VisualItem {

    public int tankId;
    public static final long fixedDelayTime = 50;
    public static final long delayStartTime = 50;

    public Bullet(int sx, int sy, int direct) {
        this.x = sx;
        this.y = sy;
        this.direct = direct;
        this.alive = true;
        this.speed = 6;

        this.setFixedDelayTime(fixedDelayTime);
    }

    @Override
    public void run() {
        if (PlayStageMgr.pause) {
            return;
        }

        this.move();

        //判断子弹是否碰到边缘 或者命中基地
        if (PlayStageMgr.instance.willInBorder(this) || RoundMapMgr.instance.border.hitHome(x, y)) {
            this.alive = false;
            this.stop();
        }
    }

    @Override
    public void drawSelf(Graphics g) {
        g.draw3DRect(this.x, this.y, 1, 1, false);
    }
}
