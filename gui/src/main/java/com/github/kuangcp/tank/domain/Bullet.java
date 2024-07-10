package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.AbstractLoopEvent;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 子弹
 * <p>
 * TODO 对象池（弹夹） 复用对象
 * TODO 抽象类，多种子弹
 */
@Slf4j
public class Bullet extends AbstractLoopEvent implements VisualItem {

    public int tankId;
    public int sx;
    public int sy;
    public int direct;
    @Setter
    @Getter
    public static int speed = 3;//如果改动要记得按钮事件里也要改
    public boolean alive = true;//是否还活着

    public static final long fixedDelayTime = 50;
    public static final long delayStartTime = 50;

    public Bullet(int sx, int sy, int direct) {
        this.sx = sx;
        this.sy = sy;
        this.direct = direct;
        this.alive = true;

        this.setFixedDelayTime(fixedDelayTime);
    }

    @Override
    public void run() {
        newRun();
    }

    private void newRun() {
        if (PlayStageMgr.pause) {
            return;
        }
        switch (direct) {
            //上下左右
            case 0:
                sy -= speed;
                break;
            case 1:
                sy += speed;
                break;
            case 2:
                sx -= speed;
                break;
            case 3:
                sx += speed;
                break;
        }

        final boolean hitHome = sx < 440 && sx > 380 && sy < 540 && sy > 480;
        //判断子弹是否碰到边缘 或者命中基地
        if (PlayStageMgr.instance.willInBorder(this) || hitHome) {
            this.alive = false;
            this.stop();
        }
        //        log.info("bullet die");
    }

    private void originRun() {
        do {
            // 每个子弹发射的延迟运动的时间
            TankTool.yieldMsTime(55);

            switch (direct) {
                //上下左右
                case 0:
                    sy -= speed;
                    break;
                case 1:
                    sy += speed;
                    break;
                case 2:
                    sx -= speed;
                    break;
                case 3:
                    sx += speed;
                    break;
            }

            //判断子弹是否碰到边缘
            if (sx < 20 || sx > 740 || sy < 20 || sy > 540) {
                this.alive = false;
            }

            if (sx < 440 && sx > 380 && sy < 540 && sy > 480) {
                this.alive = false;
            }
        } while (this.alive);
//        log.info("bullet die");
    }

    @Override
    public void drawSelf(Graphics g) {
        g.draw3DRect(this.sx, this.sy, 1, 1, false);
    }
}
