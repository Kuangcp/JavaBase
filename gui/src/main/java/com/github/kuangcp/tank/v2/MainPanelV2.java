package com.github.kuangcp.tank.v2;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-11 23:15
 */
@Slf4j
@SuppressWarnings("serial")
class MainPanelV2 extends JPanel implements KeyListener, Runnable {

    //定义我的坦克
    Hero hero = null;
    //定义泛型的集合类
    Vector<EnemyTank> ets = new Vector<>();

    //画板的构造函数 放图形的对象
    public MainPanelV2() {
        ets.add(new EnemyTank(250, 60, 4, DirectType.UP));
    }

    //重写paint
    public void paint(Graphics g) {
        super.paint(g);

        if (Objects.isNull(hero)) {
            hero = new Hero(20, 20, 4);
        }
        g.fillRect(0, 0, 500, 400);

        //调用函数绘画出主坦克
        hero.drawSelf(g);

        //画出子弹
        if (hero.bullet != null) {
            g.draw3DRect(hero.bullet.sx, hero.bullet.sy, 1, 1, false);
        }

        //画出敌人坦克
        for (EnemyTank s : ets) {
            s.drawSelf(g);
            LoopEventExecutor.addLoopEvent(s);
        }

        //画出砖块
        g.setColor(new Color(188, 112, 50));
        for (int i = 0; i < 9; i++) {
            g.fill3DRect(60, 60 + 10 * i, 20, 10, false);
        }

        g.setColor(Color.yellow);
        g.drawRect(0, 0, 400, 300);
    }

    //当按下键盘上的键时监听者的处理函数
    public void keyPressed(KeyEvent e) {
        //加了if条件后 实现了墙的限制（如果是游戏中的道具，该怎么办）
        if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(2);
            if ((hero.getX() - 10) > 0)
                hero.moveLeft();

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(3);
            if ((hero.getX() + 15) < 405)
                hero.moveRight();
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if ((hero.getY() - 13) > 0)
                hero.moveUp();

        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(1);
            if ((hero.getY() - 15) < 275)
                hero.moveDown();

        }
        //必须重新绘制窗口，不然上面的方法不能视觉上动起来
        this.repaint();
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void run() {
        int maxFPS = 60;
        long fpsTime = (long) ((1000.0 / maxFPS) * 1_000_000);

        long now;
        //每隔100ms重绘
        while (true) {
            now = System.nanoTime();

            // 进行重绘
            this.repaint();
            final long waste = System.nanoTime() - now;
            if (waste > fpsTime) {
                continue;
            }

            TankTool.yieldTime(fpsTime - waste, TimeUnit.NANOSECONDS);
            if (Objects.nonNull(hero) && !hero.isAlive()) {
                break;
            }
        }
    }
}
