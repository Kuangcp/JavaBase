package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.v1.Brick;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v1.Hinder;
import com.github.kuangcp.tank.v1.Tank;
import com.github.kuangcp.tank.v2.Shot;
import com.github.kuangcp.tank.v3.Bomb;

import java.util.Vector;

public class TankTool {

    /**
     * 工具类-检测爆炸的函数
     */
    public static void Bong(Tank t, Shot s, Vector<Bomb> bombs) {
        /*
         * 形参： 坦克 子弹 炸弹集合
         */

//		System.out.println("t.getLife()"+t.getLife());
        switch (t.getDirect()) {//上下左右
            case 0:
            case 1:
                if (t.getX() - 10 <= s.sx &&
                        t.getX() + 10 >= s.sx &&
                        t.getY() - 15 <= s.sy &&
                        t.getY() + 15 >= s.sy) {
                    s.isLive = false;
                    t.setLife(t.getLife() - 1);//生命值减一
//		    	System.out.println("减一");

                    if (t.getLife() == 0) t.setLive(false);
                    //创建一个炸弹，放入集合
                    Bomb b = new Bomb(t.getX() - 10, t.getY() - 15);//敌方的坐标
                    bombs.add(b);
                    if (t instanceof Hero) {
                        t.setX(480);
                        t.setY(500);
                        t.setDirect(0);
//			    	try {
//						Thread.sleep(300);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
                    }
                }
                break;
            case 2:
            case 3:
                if (t.getX() - 15 <= s.sx &&
                        t.getX() + 15 >= s.sx &&
                        t.getY() - 10 <= s.sy &&
                        t.getY() + 10 >= s.sy) {
                    s.isLive = false;
                    t.setLife(t.getLife() - 1);
//			    	System.out.println("减一");

                    if (t.getLife() == 0) t.setLive(false);
                    //创建一个炸弹，放入集合

                    Bomb b = new Bomb(t.getX() - 15, t.getY() - 10);//敌方的坐标
                    bombs.add(b);
                    if (t instanceof Hero) {
                        t.setX(480);
                        t.setY(500);
                        t.setDirect(0);
//			    	try {
//						Thread.sleep(300);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
                    }
                }
                break;

        }
//		System.out.println("hero life :"+t.getLife());

    }

    /**
     * 工具类-坦克之间的碰撞检测函数
     */
    public static boolean Rush(Tank me, Tank you) {

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
     * 工具类  检测是否有障碍物是否可以通行
     */
    public static boolean hasHint(Tank t, Hinder h) {
        int hx = 20, hy = 10;
        switch (t.getDirect()) {
            case 0://上
                if (t.getX() - 10 >= h.getHx() && t.getX() - 10 <= h.getHx() + hx
                        && t.getY() - 15 >= h.getHy() && t.getY() - 15 <= h.getHy() + hy
                        || t.getX() >= h.getHx() && t.getX() <= h.getHx() + hx
                        && t.getY() - 15 >= h.getHy() && t.getY() - 15 <= h.getHy() + hy
                        || t.getX() + 10 >= h.getHx() && t.getX() + 10 <= h.getHx() + hx
                        && t.getY() - 15 >= h.getHy() && t.getY() - 15 <= h.getHy() + hy)
                    return true;
            case 1://下
                if (t.getX() - 10 >= h.getHx() && t.getX() - 10 <= h.getHx() + hx
                        && t.getY() + 15 >= h.getHy() && t.getY() + 15 <= h.getHy() + hy
                        || t.getX() >= h.getHx() && t.getX() <= h.getHx() + hx
                        && t.getY() + 15 >= h.getHy() && t.getY() + 15 <= h.getHy() + hy
                        || t.getX() + 10 >= h.getHx() && t.getX() + 10 <= h.getHx() + hx
                        && t.getY() + 15 >= h.getHy() && t.getY() + 15 <= h.getHy() + hy)
                    return true;
            case 2:
                if (t.getX() - 15 >= h.getHx() && t.getX() - 15 <= h.getHx() + hx
                        && t.getY() - 10 >= h.getHy() && t.getY() - 10 <= h.getHy() + hy
                        || t.getX() - 15 >= h.getHx() && t.getX() - 15 <= h.getHx() + hx
                        && t.getY() >= h.getHy() && t.getY() <= h.getHy() + hy
                        || t.getX() - 15 >= h.getHx() && t.getX() - 15 <= h.getHx() + hx
                        && t.getY() + 10 >= h.getHy() && t.getY() + 10 <= h.getHy() + hy)
                    return true;
            case 3:
                if (t.getX() + 15 >= h.getHx() - 2 && t.getX() + 15 <= h.getHx() + hx
                        && t.getY() + 10 >= h.getHy() && t.getY() + 10 <= h.getHy() + hy
                        || t.getX() + 15 >= h.getHx() - 2 && t.getX() + 15 <= h.getHx() + hx
                        && t.getY() >= h.getHy() && t.getY() <= h.getHy() + hy
                        || t.getX() + 15 >= h.getHx() - 2 && t.getX() + 15 <= h.getHx() + hx
                        && t.getY() - 10 >= h.getHy() && t.getY() - 10 <= h.getHy() + hy)
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
                h.setLive(false);
            }
        }
    }
}
