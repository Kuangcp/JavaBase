package com.github.kuangcp.tank.v2;

import lombok.extern.slf4j.Slf4j;

/**
 * 子弹类  对象做成了线程
 */
@Slf4j
public class Shot implements Runnable {

    public int sx;
    public int sy;
    public int direct;
    public static int speed = 8;//如果改动要记得按钮事件里也要改
    public boolean isLive = true;//是否还活着

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int s) {
        speed = s;
    }

    public Shot(int sx, int sy, int direct) {
        this.sx = sx;
        this.sy = sy;
        this.direct = direct;
    }

    public void run() {
        //延迟子弹发射时间
        try {
            Thread.sleep(20);
        } catch (Exception e) {
            log.error("", e);
        }

        do {
            try {
                Thread.sleep(50); //每个子弹发射的延迟运动的时间
            } catch (InterruptedException i) {
                break;
            } catch (Exception e) {
                log.error("", e);
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

            //判断子弹是否碰到边缘
            if (sx < 20 || sx > 740 || sy < 20 || sy > 540) {
                this.isLive = false;
            }
            if (sx < 440 && sx > 380 && sy < 540 && sy > 480) {
                this.isLive = false;
            }
        } while (this.isLive);
    }
}
