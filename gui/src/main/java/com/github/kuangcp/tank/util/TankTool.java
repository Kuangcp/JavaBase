package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Hinder;
import com.github.kuangcp.tank.domain.Shot;
import com.github.kuangcp.tank.domain.Tank;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TankTool {

    /**
     * 碰撞检测函数 坦克之间
     */
    public static boolean ablePass(Tank me, Tank you) {
        if (Objects.isNull(me) || Objects.isNull(you)) {
            return true;
        }
        if (!me.isAlive() || !you.isAlive()) {
            return true;
        }

        boolean flag = true;
        switch (you.getDirect()) {//对方 上下
            case 0:
            case 1:
                switch (me.getDirect()) {//己方 上下左右 分开
                    case 0: {
                        if ((you.getX() - 10 <= me.getX() - 10 &&
                                you.getX() + 10 >= me.getX() - 10 &&
                                you.getY() - 15 <= me.getY() - 15 &&
                                you.getY() + 15 >= me.getY() - 15)
                                ||
                                (you.getX() - 10 <= me.getX() + 10 &&
                                        you.getX() + 10 >= me.getX() + 10 &&
                                        you.getY() - 15 <= me.getY() - 15 &&
                                        you.getY() + 15 >= me.getY() - 15))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 1: {
                        if ((you.getX() - 10 <= me.getX() - 10 &&
                                you.getX() + 10 >= me.getX() - 10 &&
                                you.getY() - 15 <= me.getY() + 15 &&
                                you.getY() + 15 >= me.getY() + 15)
                                ||
                                (you.getX() - 10 <= me.getX() + 10 &&
                                        you.getX() + 10 >= me.getX() + 10 &&
                                        you.getY() - 15 <= me.getY() + 15 &&
                                        you.getY() + 15 >= me.getY() + 15))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 2: {
                        if ((you.getX() - 10 <= me.getX() - 15 &&
                                you.getX() + 10 >= me.getX() - 15 &&
                                you.getY() - 15 <= me.getY() - 10 &&
                                you.getY() + 15 >= me.getY() - 10)
                                ||
                                (you.getX() - 10 <= me.getX() - 15 &&
                                        you.getX() + 10 >= me.getX() - 15 &&
                                        you.getY() - 15 <= me.getY() + 10 &&
                                        you.getY() + 15 >= me.getY() + 10))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 3: {
                        if ((you.getX() - 10 <= me.getX() + 15 &&
                                you.getX() + 10 >= me.getX() + 15 &&
                                you.getY() - 15 <= me.getY() - 10 &&
                                you.getY() + 15 >= me.getY() - 10)
                                ||
                                (you.getX() - 10 <= me.getX() + 15 &&
                                        you.getX() + 10 >= me.getX() + 15 &&
                                        you.getY() - 15 <= me.getY() + 10 &&
                                        you.getY() + 15 >= me.getY() + 10))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    default:
                        break;
                }

            case 2:
            case 3://对方左右
                switch (me.getDirect()) {//己方 上下左右 分开
                    case 0: {
                        if ((you.getX() - 15 <= me.getX() - 10 &&
                                you.getX() + 15 >= me.getX() - 10 &&
                                you.getY() - 10 <= me.getY() - 15 &&
                                you.getY() + 10 >= me.getY() - 15)
                                ||
                                (you.getX() - 15 <= me.getX() + 10 &&
                                        you.getX() + 15 >= me.getX() + 10 &&
                                        you.getY() - 10 <= me.getY() - 15 &&
                                        you.getY() + 10 >= me.getY() - 15))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 1: {
                        if ((you.getX() - 15 <= me.getX() - 10 &&
                                you.getX() + 15 >= me.getX() - 10 &&
                                you.getY() - 10 <= me.getY() + 15 &&
                                you.getY() + 10 >= me.getY() + 15)
                                ||
                                (you.getX() - 15 <= me.getX() + 10 &&
                                        you.getX() + 15 >= me.getX() + 10 &&
                                        you.getY() - 10 <= me.getY() + 15 &&
                                        you.getY() + 10 >= me.getY() + 15))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 2: {
                        if ((you.getX() - 15 <= me.getX() - 15 &&
                                you.getX() + 15 >= me.getX() - 15 &&
                                you.getY() - 10 <= me.getY() - 10 &&
                                you.getY() + 10 >= me.getY() - 10)
                                ||
                                (you.getX() - 15 <= me.getX() - 15 &&
                                        you.getX() + 15 >= me.getX() - 15 &&
                                        you.getY() - 10 <= me.getY() + 10 &&
                                        you.getY() + 10 >= me.getY() + 10))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    case 3: {
                        if ((you.getX() - 10 <= me.getX() + 15 &&
                                you.getX() + 10 >= me.getX() + 15 &&
                                you.getY() - 15 <= me.getY() - 10 &&
                                you.getY() + 15 >= me.getY() - 10)
                                ||
                                (you.getX() - 10 <= me.getX() + 15 &&
                                        you.getX() + 10 >= me.getX() + 15 &&
                                        you.getY() - 15 <= me.getY() + 10 &&
                                        you.getY() + 15 >= me.getY() + 10))//判断最前方两个点是否在对方坦克区域内
                        {
                            flag = false;
                        }
                        break;
                    }
                    default:
                        break;
                }
        }
        //switch 语句之外：
        return flag;
    }

    /**
     * 碰撞检测函数 坦克 和 障碍物
     */
    public static boolean ablePass(Tank t, Hinder h) {
        int hx = 20, hy = 10;
        final int halfHeight = t.getHalfHeight();
        final int halfWidth = t.getHalfWidth();

        switch (t.getDirect()) {
            case DirectType.UP:
                if (t.getX() - halfWidth >= h.getHx() && t.getX() - halfWidth <= h.getHx() + hx
                        && t.getY() - halfHeight >= h.getHy() && t.getY() - halfHeight <= h.getHy() + hy
                        || t.getX() >= h.getHx() && t.getX() <= h.getHx() + hx
                        && t.getY() - halfHeight >= h.getHy() && t.getY() - halfHeight <= h.getHy() + hy
                        || t.getX() + halfWidth >= h.getHx() && t.getX() + halfWidth <= h.getHx() + hx
                        && t.getY() - halfHeight >= h.getHy() && t.getY() - halfHeight <= h.getHy() + hy)
                    return true;
            case DirectType.DOWN:
                if (t.getX() - halfWidth >= h.getHx() && t.getX() - halfWidth <= h.getHx() + hx
                        && t.getY() + halfHeight >= h.getHy() && t.getY() + halfHeight <= h.getHy() + hy
                        || t.getX() >= h.getHx() && t.getX() <= h.getHx() + hx
                        && t.getY() + halfHeight >= h.getHy() && t.getY() + halfHeight <= h.getHy() + hy
                        || t.getX() + halfWidth >= h.getHx() && t.getX() + halfWidth <= h.getHx() + hx
                        && t.getY() + halfHeight >= h.getHy() && t.getY() + halfHeight <= h.getHy() + hy)
                    return true;
            case DirectType.LEFT:
                if (t.getX() - halfHeight >= h.getHx() && t.getX() - halfHeight <= h.getHx() + hx
                        && t.getY() - halfWidth >= h.getHy() && t.getY() - halfWidth <= h.getHy() + hy
                        || t.getX() - halfHeight >= h.getHx() && t.getX() - halfHeight <= h.getHx() + hx
                        && t.getY() >= h.getHy() && t.getY() <= h.getHy() + hy
                        || t.getX() - halfHeight >= h.getHx() && t.getX() - halfHeight <= h.getHx() + hx
                        && t.getY() + halfWidth >= h.getHy() && t.getY() + halfWidth <= h.getHy() + hy)
                    return true;
            case DirectType.RIGHT:
                if (t.getX() + halfHeight >= h.getHx() - 2 && t.getX() + halfHeight <= h.getHx() + hx
                        && t.getY() + halfWidth >= h.getHy() && t.getY() + halfWidth <= h.getHy() + hy
                        || t.getX() + halfHeight >= h.getHx() - 2 && t.getX() + halfHeight <= h.getHx() + hx
                        && t.getY() >= h.getHy() && t.getY() <= h.getHy() + hy
                        || t.getX() + halfHeight >= h.getHx() - 2 && t.getX() + halfHeight <= h.getHx() + hx
                        && t.getY() - halfWidth >= h.getHy() && t.getY() - halfWidth <= h.getHy() + hy)
                    return true;
        }
        return false;
    }

    /**
     * 子弹和障碍物 碰撞监测
     */
    public static void judgeHint(Shot s, Hinder h) {
        if (s.sx >= h.getHx() - 1 && s.sx <= h.getHx() + 20
                && s.sy >= h.getHy() - 1 && s.sy <= h.getHy() + 10) {
            s.isLive = false;

            if (h instanceof Brick) {
                h.setAlive(false);
            }
        }
    }

    public static void yieldMsTime(long time) {
        yieldTime(time, TimeUnit.MILLISECONDS);
    }

    public static void yieldTime(long time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }
}