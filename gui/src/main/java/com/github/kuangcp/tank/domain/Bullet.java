package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.util.TankTool;
import lombok.extern.slf4j.Slf4j;

/**
 * 子弹类  对象做成了线程
 */
@Slf4j
public class Bullet implements Runnable {

    public int sx;
    public int sy;
    public int direct;
    public static int speed = 3;//如果改动要记得按钮事件里也要改
    public boolean isLive = true;//是否还活着

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int s) {
        speed = s;
    }

    public Bullet(int sx, int sy, int direct) {
        this.sx = sx;
        this.sy = sy;
        this.direct = direct;
    }

    public void run() {
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
                this.isLive = false;
            }

            if (sx < 440 && sx > 380 && sy < 540 && sy > 480) {
                this.isLive = false;
            }
        } while (this.isLive);
        log.info("shot die");
    }
}
