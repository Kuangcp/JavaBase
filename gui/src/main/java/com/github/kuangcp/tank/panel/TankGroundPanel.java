package com.github.kuangcp.tank.panel;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.StageBorder;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.frame.MainFrame;
import com.github.kuangcp.tank.frame.SettingFrame;
import com.github.kuangcp.tank.mgr.BombMgr;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
import com.github.kuangcp.tank.resource.ColorMgr;
import com.github.kuangcp.tank.util.HoldingKeyStateMgr;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TankGroundPanel extends JPanel implements java.awt.event.KeyListener, Runnable {

    private void drawBg(Graphics g) {
        g.setColor(ColorMgr.instance.bgColor);
        final StageBorder border = RoundMapMgr.instance.border;
        g.fillRect(0, 0, border.getMinX() + border.getMaxX(), border.getMinY() + border.getMaxY());
        g.setColor(Color.green);
        g.drawRect(border.getMinX(), border.getMinY(),
                border.getMaxX() - border.getMinX(), border.getMaxY() - border.getMinY());
    }

    private void drawHeroInfo(Graphics g) {
        g.setColor(Color.GREEN);
        final String lifeInfo = "Life:" + PlayStageMgr.hero.getLife()
                + " Enemy: " + PlayStageMgr.instance.getLiveEnemy()
                + " Prize: " + PlayStageMgr.hero.getPrize();
        g.drawString(lifeInfo, RoundMapMgr.instance.border.getMinX(), 15);
    }

    private void drawMonitorInfo(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(MonitorExecutor.info.toString(),
                RoundMapMgr.instance.border.getMinX(), RoundMapMgr.instance.border.getTotalY() - 3);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (PlayStageMgr.stageNoneStart()) {
            PlayStageMgr.drawStopIgnore(g, this);
            return;
        }

        this.drawBg(g);
        this.drawHeroInfo(g);
        this.drawMonitorInfo(g);


        PlayStageMgr.instance.refresh();

        g.setColor(ColorMgr.instance.ironColor);
        final List<Iron> irons = PlayStageMgr.irons;
        for (Iron ir : irons) {
            g.fill3DRect(ir.getHx(), ir.getHy(), ir.getWidth(), ir.getHeight(), false);
        }

        g.setColor(ColorMgr.instance.brickColor);
        final List<Brick> bricks = PlayStageMgr.bricks;
        for (Brick bs : bricks) {
            g.fill3DRect(bs.getHx(), bs.getHy(), bs.getWidth(), bs.getHeight(), false);
        }

        /*画出头像*/
        // fixme 像素偏移
        g.drawImage(AvatarImgMgr.instance.curImg, 380, 480,
                AvatarImgMgr.instance.width, AvatarImgMgr.instance.height, this);

        /*画出主坦克*/
        final List<EnemyTank> enemyList = PlayStageMgr.enemyList;
        if (PlayStageMgr.hero.isAlive()) {
            for (EnemyTank enemyTank : enemyList) {
                BombMgr.instance.checkBong(PlayStageMgr.hero, enemyTank.bulletList);
            }

            PlayStageMgr.hero.drawSelf(g);
        }

        // hero bullet
        for (Bullet bullet : PlayStageMgr.hero.bulletList) {
            g.setColor(Color.YELLOW);
            g.draw3DRect(bullet.sx, bullet.sy, 3, 3, false);
        }

        enemyList.stream().filter(Tank::isAlive).forEach(t -> t.drawSelf(g));

        // FIXME ConcurrentModificationException
        for (EnemyTank et : enemyList) {
            for (Bullet bullet : et.bulletList) {
                g.setColor(Color.cyan);
                g.draw3DRect(bullet.sx, bullet.sy, 1, 1, false);
            }
        }
        BombMgr.instance.drawBomb(g, this);

        if (PlayStageMgr.instance.hasWinCurrentRound() || !PlayStageMgr.hero.isAlive()) {
            PlayStageMgr.instance.stopStage();
        }
    }

    /**
     * 当按下键盘上的键时监听者的处理函数
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // 启动关闭流程
        if (e.getKeyCode() == KeyEvent.VK_Q && HoldingKeyStateMgr.instance.isCtrl()) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_T && !PlayStageMgr.invokeNewStage) {
            PlayStageMgr.invokeNewStage = true;
            MainFrame.actionPanel.startNewStage();
            this.repaint();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            PlayStageMgr.pause = true;
        } else if (e.getKeyCode() == KeyEvent.VK_O) {
            PlayStageMgr.pause = true;
            SettingFrame.activeFocus();
        }

        if (PlayStageMgr.pause) {
            return;
        }
        // 实际用户交互
        if (e.getKeyCode() == KeyEvent.VK_J) {
            HoldingKeyStateMgr.instance.setShot(true);
        }

        HoldingKeyStateMgr.instance.handleDirectPress(e);
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent re) {
        HoldingKeyStateMgr.instance.handleRelease(re);

        if (re.getKeyCode() == KeyEvent.VK_T && PlayStageMgr.invokeNewStage) {
            PlayStageMgr.invokeNewStage = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        log.info("1={}", e);
    }

    //画板做成线程  画布进行刷新
    @Override
    public void run() {
        int maxFPS = 60;
        long fpsTime = (long) ((1000.0 / maxFPS) * 1000000);

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
        }
    }
}