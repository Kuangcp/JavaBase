package com.github.kuangcp.tank.mgr;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Bomb;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.resource.AbstractImgListMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author https://github.com/kuangcp on 2021-09-11 16:28
 */
@Slf4j
public class BombMgr extends AbstractImgListMgr {

    public static BombMgr instance = new BombMgr();

    public BombMgr() {
        super.imgPathArr = new String[]{"/images/bomb_1.gif", "/images/bomb_2.gif", "/images/bomb_3.gif"};
    }

    //定义炸弹爆炸集合
    public List<Bomb> bombs = Collections.synchronizedList(new ArrayList<>());

    /**
     * 画出炸弹
     */
    public void drawBomb(Graphics g, JPanel panel) {
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb b = bombs.get(i);
//			System.out.println("size = "+bombs.size());
            if (b.life > 10) {
                g.drawImage(BombMgr.instance.imgArr[0], b.bx, b.by, 30, 30, panel);
//				if(rr){
//					Audio B = new Audio("./src/RE/GameBegin.wav");
//					B.start();
//					rr = false;
//				}
            } else if (b.life > 5) {
                g.drawImage(BombMgr.instance.imgArr[1], b.bx, b.by, 30, 30, panel);
            } else {
                g.drawImage(BombMgr.instance.imgArr[2], b.bx, b.by, 30, 30, panel);
            }
            //让b的生命减少
            b.lifeDown();

            //炸弹的生命值为零 移出集合
            if (b.life == 0) {
                bombs.remove(b);
            }
//			System.out.println("size = "+bombs.size());
        }
    }

    /**
     * 工具类-检测爆炸的函数
     */
    public void checkBong(Tank tank, List<Bullet> bullets) {
        bullets.forEach(v -> this.checkBong(tank, v));
    }

    private void checkBong(Tank t, Bullet s) {
        if (!s.isLive) {
            return;
        }

        switch (t.getDirect()) {
            case DirectType.UP:
            case DirectType.DOWN:
                if (t.getX() - 10 <= s.sx &&
                        t.getX() + 10 >= s.sx &&
                        t.getY() - 15 <= s.sy &&
                        t.getY() + 15 >= s.sy) {
                    s.isLive = false;

                    t.setLife(t.getLife() - 1);

                    if (t.getLife() <= 0) {
                        t.setAlive(false);
                    }

                    //创建一个炸弹，放入集合
                    Bomb b = new Bomb(t.getX() - 10, t.getY() - 15);//敌方的坐标
                    bombs.add(b);

                    // 复活
                    if (t instanceof Hero) {
                        ((Hero) t).resurrect();
                    }
                }
                break;
            case DirectType.LEFT:
            case DirectType.RIGHT:
                if (t.getX() - 15 <= s.sx &&
                        t.getX() + 15 >= s.sx &&
                        t.getY() - 10 <= s.sy &&
                        t.getY() + 10 >= s.sy) {
                    s.isLive = false;
                    t.setLife(t.getLife() - 1);

                    if (t.getLife() == 0) t.setAlive(false);

                    //创建一个炸弹，放入集合
                    Bomb b = new Bomb(t.getX() - 15, t.getY() - 10);//敌方的坐标
                    bombs.add(b);

                    // 复活
                    if (t instanceof Hero) {
                        ((Hero) t).resurrect();
                    }
                }
                break;
        }
    }

}
