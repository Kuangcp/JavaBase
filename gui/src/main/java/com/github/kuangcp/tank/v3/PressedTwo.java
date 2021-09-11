package com.github.kuangcp.tank.v3;


import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v3.thread.ExitFlagRunnable;
import lombok.extern.slf4j.Slf4j;

/**
 * 思路如下：
 * 在按下键的pressed函数中 激活键
 * 在离开键Release函数中把离开的键从 取消激活
 * 另开一个线程来一直遍历 字段，达到同时监控两个键的的动作的效果
 * <p>
 * 实现结果： 一边跑，一边放子弹完全不是事儿，方向的切换也十分流畅
 */
@Slf4j
public class PressedTwo implements ExitFlagRunnable {

    Hero hero;
    MyPanel3 myPanel3;
    ListenEventGroup eventGroup;
    private volatile boolean exit = false;

    public PressedTwo(ListenEventGroup eventGroup, Hero hero, MyPanel3 myPanel3) {
        this.eventGroup = eventGroup;
        this.hero = hero;
        this.myPanel3 = myPanel3;
    }

    public void exit() {
        this.exit = true;
    }

    @Override
    public void run() {
        while (hero.isAlive() && !exit) {
            if (eventGroup.hasPressMoveEvent()) {
//                log.info("eventGroup={}", eventGroup);
            }
            if (eventGroup.isLeft()) {
                hero.setDirect(DirectType.LEFT);
                if ((hero.getX() - 10) > 20)
                    hero.moveleft();
            }

            if (eventGroup.isRight()) {
                hero.setDirect(DirectType.RIGHT);
                if ((hero.getX() + 15) < 742)
                    hero.moveright();
            }
            if (eventGroup.isDown()) {
                hero.setDirect(DirectType.DOWN);
                if ((hero.getY() - 15) < 515)
                    hero.movedown();
            }
            if (eventGroup.isUp()) {
                hero.setDirect(DirectType.UP);
                if ((hero.getY() - 13) > 20)
                    hero.moveup();
            }

            if (eventGroup.isShot()) {
                hero.shotEnemy();
            }

            myPanel3.repaint();

            try {
                // 动作的延迟 1000 / 77 fps
                Thread.sleep(29);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }
}
