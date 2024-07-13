package com.github.kuangcp.tank.backup.v2;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.StageBorder;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.util.TankTool;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-11 23:15
 */
@Slf4j
@SuppressWarnings("serial")
class MainPanelV2 extends JPanel implements KeyListener, Runnable {

    //定义我的坦克
    Hero hero;
    //定义泛型的集合类
    Vector<EnemyTank> ets = new Vector<>();

    //画板的构造函数 放图形的对象
    public MainPanelV2() {
//        ets.add(new EnemyTank(250, 60, 1, DirectType.UP));
//        ets.add(new EnemyTank(250, 60, 2, DirectType.UP));
//        ets.add(new EnemyTank(250, 60, 3, DirectType.UP));
//        ets.add(new EnemyTank(250, 260, 4, DirectType.UP));
//        ets.add(new EnemyTank(250, 260, 6, DirectType.UP));

        ets.add(new EnemyTank(250, 60, DirectType.UP));
        ets.add(new EnemyTank(250, 70, DirectType.UP));
        ets.add(new EnemyTank(250, 80, DirectType.UP));
        ets.add(new EnemyTank(250, 90, DirectType.UP));
        ets.add(new EnemyTank(250, 100, DirectType.UP));

//        for (int i = 0; i < 700; i++) {
//            ets.add(new EnemyTank(20 + i * 40 % RoundMapMgr.instance.border.getMaxX(), 100 + i * 5 % RoundMapMgr.instance.border.getMaxY(), DirectType.UP));
//        }

//        for (EnemyTank et : ets) {
//            LoopEventExecutor.addLoopEvent(et);
//        }
        hero = new Hero(160, 160, 4);
    }

    //重写paint
    public void paint(Graphics g) {
        super.paint(g);

        final StageBorder border = RoundMapMgr.instance.border;
        g.fillRect(0, 0, border.getMaxX() + border.getMinX(), border.getMaxY() + border.getMinY());

        //调用函数绘画出主坦克
        hero.drawSelf(g);

        //画出子弹
//        if (hero.bullet != null) {
//            g.draw3DRect(hero.bullet.sx, hero.bullet.sy, 1, 1, false);
//        }

        //画出敌人坦克
        for (EnemyTank s : ets) {
            s.drawSelf(g);
            final Iterator<Bullet> iterator = s.bulletList.iterator();
            while (iterator.hasNext()) {
                final Bullet next = iterator.next();
                if (!next.alive) {
                    iterator.remove();
                }
                g.setColor(Color.cyan);
                next.drawSelf(g);
            }
        }

        //画出砖块
        g.setColor(new Color(188, 112, 50));
        for (int i = 0; i < 9; i++) {
            g.fill3DRect(60, 60 + 10 * i, 20, 10, false);
        }

        // border
        g.setColor(Color.yellow);
        g.drawRect(border.getMinX(), border.getMinY(),
                border.getMaxX() - border.getMinX(), border.getMaxY() - border.getMinY());
    }

    //当按下键盘上的键时监听者的处理函数
    public void keyPressed(KeyEvent e) {
        //加了if条件后 实现了墙的限制（如果是游戏中的道具，该怎么办）
        if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(2);
            if (PlayStageMgr.instance.willInBorder(hero))
                hero.move();

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(3);
            if (PlayStageMgr.instance.willInBorder(hero))
                hero.move();
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if (PlayStageMgr.instance.willInBorder(hero))
                hero.move();
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(1);
            if (PlayStageMgr.instance.willInBorder(hero))
                hero.move();
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
