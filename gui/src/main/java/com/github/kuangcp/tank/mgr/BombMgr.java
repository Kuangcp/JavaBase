package com.github.kuangcp.tank.mgr;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Bomb;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.resource.AbstractImgListMgr;
import com.github.kuangcp.tank.resource.PropertiesMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-11 16:28
 */
@Slf4j
public class BombMgr extends AbstractImgListMgr {

    public static BombMgr instance = new BombMgr();

    public BombMgr() {
    }

    public String getConfigKey() {
        return PropertiesMgr.Key.Img.ANIMATION_BOMB;
    }

    //定义炸弹爆炸集合
    private final List<Bomb> bombs = new LinkedList<>();

    /**
     * 画出炸弹
     */
    public void drawBomb(Graphics g, JPanel panel) {
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb b = bombs.get(i);
            if (b.life > 10) {
                g.drawImage(BombMgr.instance.imgArr[0], b.bx, b.by, 30, 30, panel);
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
        }
    }

    /**
     * 工具类-检测爆炸的函数
     */
    public void checkBong(Tank tank, List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            this.checkBong(tank, bullet);
        }
    }

    private void checkBong(Tank tank, Bullet bullet) {
        if (!bullet.alive || !tank.isAlive()) {
            return;
        }

        switch (tank.getDirect()) {
            case DirectType.UP:
            case DirectType.DOWN:
                if (tank.getX() - 10 <= bullet.x &&
                        tank.getX() + 10 >= bullet.x &&
                        tank.getY() - 15 <= bullet.y &&
                        tank.getY() + 15 >= bullet.y) {
                    bullet.alive = false;
                    final int bx = tank.getX() - tank.getHalfWidth();
                    final int by = tank.getY() - tank.getHalfHeight();

                    this.handleBombAndTank(tank, bx, by);
                }
                break;
            case DirectType.LEFT:
            case DirectType.RIGHT:
                if (tank.getX() - 15 <= bullet.x &&
                        tank.getX() + 15 >= bullet.x &&
                        tank.getY() - 10 <= bullet.y &&
                        tank.getY() + 10 >= bullet.y) {
                    bullet.alive = false;
                    final int bx = tank.getX() - tank.getHalfHeight();
                    final int by = tank.getY() - tank.getHalfWidth();

                    this.handleBombAndTank(tank, bx, by);
                    break;
                }
                break;
        }
    }

    private void handleBombAndTank(Tank tank, int bx, int by) {
        if (tank.hitByBullet()) {
            bombs.add(new Bomb(bx, by));
        }
    }
}
