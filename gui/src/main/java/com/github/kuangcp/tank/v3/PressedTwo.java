package com.github.kuangcp.tank.v3;


import com.github.kuangcp.tank.v1.Hero;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Vector;

/**
 * 十分感谢网上这位的算法，使用了之后，坦克根本就不会卡，行云流水般顺畅
 * 思路如下：
 * 在按下键的pressed函数中创建一个该键值的对象，加入集合中，
 * 在离开键Release函数中把离开的键从集合中清除
 * 另开一个线程来一直遍历这个集合，达到同时监控两个键的的动作的效果
 * 实现结果： 一边跑，一边放子弹完全不是事儿，方向的切换也十分流畅
 */
@Slf4j
public class PressedTwo implements Runnable {

    Vector<KE> actions;
    Hero hero;
    MyPanel3 myPanel3;

    public PressedTwo(Vector<KE> actions, Hero hero, MyPanel3 myPanel3) {
        this.actions = actions;
        this.hero = hero;
        this.myPanel3 = myPanel3;
    }

    @Override
    public void run() {
        while (true) {
            int round = 0;
            for (KE p : actions) {
                if (Objects.isNull(p)) {
                    continue;
                }

                switch (p.keyCode) {
                    case KeyEvent.VK_A:
                        hero.setDirect(2);
                        if ((hero.getX() - 10) > 20)
                            hero.moveleft();
                        break;
                    case KeyEvent.VK_D:
                        hero.setDirect(3);
                        if ((hero.getX() + 15) < 742)
                            hero.moveright();
                        break;
                    case KeyEvent.VK_S:
                        hero.setDirect(1);
                        if ((hero.getY() - 15) < 515)
                            hero.movedown();
                        break;
                    case KeyEvent.VK_W:
                        hero.setDirect(0);
                        if ((hero.getY() - 13) > 20)
                            hero.moveup();
                        break;
                    case KeyEvent.VK_J:
                        hero.shotEnemy();
                        break;
                    default:
                        break;
                }
                myPanel3.repaint();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }

            if (!hero.getisLive()) {
                break;
            }
            round++;
            if (round > 10000) {
                round -= 10000;
            }
        }
    }
}
