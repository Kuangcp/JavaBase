package com.github.kuangcp.tank.util;


import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import com.github.kuangcp.tank.thread.ExitFlagRunnable;
import com.github.kuangcp.tank.v3.PlayStageMgr;
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
public class KeyListener implements ExitFlagRunnable {

    Hero hero;
    TankGroundPanel tankGroundPanel;
    HoldingKeyEventMgr eventGroup;
    private volatile boolean exit = false;

    public KeyListener(HoldingKeyEventMgr eventGroup, Hero hero, TankGroundPanel tankGroundPanel) {
        this.eventGroup = eventGroup;
        this.hero = hero;
        this.tankGroundPanel = tankGroundPanel;
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
                if (PlayStageMgr.instance.willInBorder(hero) && PlayStageMgr.instance.ableToMove(hero)) {
                    hero.moveLeft();
                }
            } else if (eventGroup.isRight()) {
                hero.setDirect(DirectType.RIGHT);
                if (PlayStageMgr.instance.willInBorder(hero) && PlayStageMgr.instance.ableToMove(hero)) {
                    hero.moveRight();
                }
            } else if (eventGroup.isDown()) {
                hero.setDirect(DirectType.DOWN);
                if (PlayStageMgr.instance.willInBorder(hero) && PlayStageMgr.instance.ableToMove(hero)) {
                    hero.moveDown();
                }
            } else if (eventGroup.isUp()) {
                hero.setDirect(DirectType.UP);
                if (PlayStageMgr.instance.willInBorder(hero) && PlayStageMgr.instance.ableToMove(hero)) {
                    hero.moveUp();
                }
            }

            if (eventGroup.isShot()) {
                hero.shotEnemy();
            }

            // 动作的延迟 1000 / 77 fps
            TankTool.yieldMsTime(33);
        }
    }
}
