package com.github.kuangcp.tank.resource;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.domain.Shot;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author https://github.com/kuangcp on 2021-09-11 16:28
 */
@Slf4j
public class BombMgr {

    public static BombMgr instance = new BombMgr();

    public Image[] bombArr = new Image[3];
    //定义炸弹爆炸集合
    public List<Bomb> bombs = Collections.synchronizedList(new ArrayList<>());

    public void initImg() {
        try {
            // 爆炸三阶段
            for (int i = 1; i <= 3; i++) {
                bombArr[i - 1] = ImageIO.read(getClass().getResourceAsStream("/images/bomb_" + i + ".gif"));
            }
//            bombArr[0] = ImageIO.read(getClass().getResourceAsStream("/images/bomb_1.gif"));
//            bombArr[1] = ImageIO.read(getClass().getResourceAsStream("/images/bomb_2.gif"));
//            bombArr[2] = ImageIO.read(getClass().getResourceAsStream("/images/bomb_3.gif"));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 画出炸弹
     */
    public void drawBomb(Graphics g, JPanel panel) {
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb b = bombs.get(i);
//			System.out.println("size = "+bombs.size());
            if (b.life > 10) {
                g.drawImage(BombMgr.instance.bombArr[0], b.bx, b.by, 30, 30, panel);
//				if(rr){
//					Audio B = new Audio("./src/RE/GameBegin.wav");
//					B.start();
//					rr = false;
//				}
            } else if (b.life > 5) {
                g.drawImage(BombMgr.instance.bombArr[1], b.bx, b.by, 30, 30, panel);
            } else {
                g.drawImage(BombMgr.instance.bombArr[2], b.bx, b.by, 30, 30, panel);
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
    public void checkBong(Tank tank, List<Shot> shots) {
        shots.forEach(v -> this.checkBong(tank, v));
    }

    private void checkBong(Tank t, Shot s) {
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
                    if (t instanceof Hero) {
                        t.setX(480);
                        t.setY(500);
                        t.setDirect(0);
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
                    if (t instanceof Hero) {
                        t.setX(480);
                        t.setY(500);
                        t.setDirect(0);
                    }
                }
                break;
        }
    }

}
