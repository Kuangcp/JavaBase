package com.github.kuangcp.tank.panel;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.StageBorder;
import com.github.kuangcp.tank.frame.MainFrame;
import com.github.kuangcp.tank.frame.SettingFrame;
import com.github.kuangcp.tank.mgr.BombMgr;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
import com.github.kuangcp.tank.resource.ColorMgr;
import com.github.kuangcp.tank.util.ExecutePool;
import com.github.kuangcp.tank.util.HoldingKeyStateMgr;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.AbstractDelayEvent;
import com.github.kuangcp.tank.util.executor.DelayExecutor;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TankGroundPanel extends JPanel implements java.awt.event.KeyListener, Runnable {

    public volatile Hero hero;
    public static boolean newStage = true;
    private volatile boolean invokeNewStage = false;

    public List<EnemyTank> enemyList = new CopyOnWriteArrayList<>();
    public List<Brick> bricks = new CopyOnWriteArrayList<>();
    public List<Iron> irons = new CopyOnWriteArrayList<>();

    //所有按下键的code集合
    public static int[][] enemyTankMap = new int[12][2];
    public static int[] myself = new int[6];

    public TankGroundPanel() {

    }

    public void startNewRound() {
        enemyList = new CopyOnWriteArrayList<>();
        bricks = new CopyOnWriteArrayList<>();
        irons = new CopyOnWriteArrayList<>();

        //创建英雄坦克
        if (newStage) {
            hero = new Hero(480, 500, 3);
            hero.setLife(10);
        } else {
            hero = new Hero(myself[0], myself[1], 3);
            hero.setLife(myself[2]);
            hero.setPrize(myself[3]);
        }

        PlayStageMgr.init(hero, enemyList, bricks, irons);

        //多键监听实现
//        heroKeyListener = new HeroKeyListener(HoldingKeyEventMgr.instance, hero, this);
//        ExecutePool.exclusiveLoopPool.execute(heroKeyListener);
        ExecutePool.exclusiveLoopPool.execute(hero);

        // 创建 敌人的坦克
        EnemyTank ett = null;
        if (newStage) {//正常启动并创建坦克线程
            for (int i = 0; i < PlayStageMgr.getEnemySize(); i++) {
                //在四个随机区域产生坦克
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 1:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 2:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 3:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), i % 4);
                        break;
                }

                if (Objects.isNull(ett)) {
                    continue;
                }
                LoopEventExecutor.addLoopEvent(ett);
                enemyList.add(ett);
            }
        } else {
            /*进入读取文件步骤*/
            for (int i = 0; i < enemyTankMap.length; i++) {
                if (enemyTankMap[i][0] == 0) {
                    break;
                }
                ett = new EnemyTank(enemyTankMap[i][0], enemyTankMap[i][1], i % 4);
                LoopEventExecutor.addLoopEvent(ett);
                enemyList.add(ett);
            }
        }

        //左右下角
        createB(bricks, 20, 310, 300, 540);
        createB(bricks, 520, 310, 740, 540);

        //头像附近
        createB(bricks, 360, 460, 460, 480);
        createB(bricks, 360, 480, 380, 540);
        createB(bricks, 440, 460, 460, 540);

        createI(irons, 330, 410, 480, 430);

        PlayStageMgr.instance.markStartLogic();
    }

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
        final String lifeInfo = "Life:" + hero.getLife()
                + " Enemy: " + PlayStageMgr.instance.getLiveEnemy()
                + " Prize: " + hero.getPrize();
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

        if (PlayStageMgr.stageNoneStart() || Objects.isNull(hero)) {
            PlayStageMgr.drawStopIgnore(g, this);
            return;
        }

        this.drawBg(g);
        this.drawHeroInfo(g);
        this.drawMonitorInfo(g);

        /*画出障碍物__砖__ 铁__*/

        g.setColor(ColorMgr.instance.ironColor);
        for (int i = 0; i < irons.size(); i++) {
            Iron ir = irons.get(i);
            if (ir.getAlive()) {
                g.fill3DRect(ir.getHx(), ir.getHy(), ir.getWidth(), ir.getHeight(), false);
            } else {
                irons.remove(ir);
            }
        }

        g.setColor(ColorMgr.instance.brickColor);
        for (int i = 0; i < bricks.size(); i++) {
            Brick bs = bricks.get(i);
            if (bs.getAlive()) {
                g.fill3DRect(bs.getHx(), bs.getHy(), bs.getWidth(), bs.getHeight(), false);
            } else {
                bricks.remove(bs);
            }
        }

        /*画出头像*/
        g.drawImage(AvatarImgMgr.instance.curImg, 380, 480,
                AvatarImgMgr.instance.width, AvatarImgMgr.instance.height, this);

        /*画出主坦克*/
        if (hero.isAlive()) {
            for (int i = 0; i < enemyList.size(); i++) {
                EnemyTank demon;
                try {
                    demon = enemyList.get(i);
                } catch (IndexOutOfBoundsException e) {
                    log.error("", e);
                    continue;
                }

                BombMgr.instance.checkBong(hero, demon.bulletList);
            }

            hero.drawSelf(g);
        }

        // 画出自己的子弹画子弹是可以封装成一个方法的
        // 从ss 这个子弹集合中 取出每颗子弹，并画出来
        for (int i = 0; i < hero.bulletList.size(); i++) {
            Bullet myBullet = hero.bulletList.get(i);
            for (Brick brick : bricks) {
                TankTool.judgeHint(myBullet, brick);
            }
            for (Iron iron : irons) {
                TankTool.judgeHint(myBullet, iron);
            }
            if (myBullet.sx < 440 && myBullet.sx > 380 && myBullet.sy < 540 && myBullet.sy > 480) {
                myBullet.alive = false;
                hero.setAlive(false);
            }
            if (hero.bulletList.get(i) != null && hero.bulletList.get(i).alive) {
                g.setColor(Color.YELLOW);
                g.draw3DRect(myBullet.sx, myBullet.sy, 3, 3, false);
            }

            //子弹线程死了 就要把它从集合中删除
            if (!myBullet.alive) {
                hero.bulletList.remove(myBullet);
            }
        }

        /*敌人子弹*/
        // FIXME ConcurrentModificationException
        for (int j = 0; j < enemyList.size(); j++) {
            EnemyTank et;
            try {
                et = enemyList.get(j);
            } catch (IndexOutOfBoundsException e) {
                log.error("", e);
                continue;
            }

            for (int i = 0; i < et.bulletList.size(); i++) {
                Bullet myBullet = et.bulletList.get(i);
                for (Brick brick : bricks) {
                    TankTool.judgeHint(myBullet, brick);
                }
                for (Iron iron : irons) {
                    TankTool.judgeHint(myBullet, iron);
                }
                if (myBullet.sx < 440 && myBullet.sx > 380 && myBullet.sy < 540 && myBullet.sy > 480) {
                    myBullet.alive = false;
                    hero.setAlive(false);
                }
                if (et.bulletList.get(i) != null && et.bulletList.get(i).alive) {
                    g.setColor(Color.cyan);
                    g.draw3DRect(myBullet.sx, myBullet.sy, 1, 1, false);

                }
                if (!myBullet.alive) {
                    et.bulletList.remove(myBullet);
                }
            }
        }

        BombMgr.instance.drawBomb(g, this);

        /*画出敌人坦克*/
        //坦克少于5个就自动添加4个
        if (enemyList.size() < 5) {
            for (int i = 0; i < 4; i++) {
                EnemyTank d = new EnemyTank(20 + (int) (Math.random() * 400), 20 + (int) (Math.random() * 300), i % 4);
                LoopEventExecutor.addLoopEvent(d);
//                Thread fillThread = new Thread(d);
//                fillThread.setName("fillEnemy" + d.id);
//                fillThread.start();
                enemyList.add(d);
            }
        }


//		for(int i=0;i<ets.size();i++){
//			Demons s = ets.get(i);
//			if(s.getisLive()){
//				for (int j=0;j<hero.ss.size();j++)
//				     Tool.Bong(s, hero.ss.get(j), bombs);
//			    this.drawTank(s.getX(), s.getY(), g, s.getDirect(), s.getType());
//			    
//			}else {
//	        	ets.remove(s);
//			}
//		}
        for (int i = 0; i < enemyList.size(); i++) {
            EnemyTank demon;
            try {
                demon = enemyList.get(i);
            } catch (IndexOutOfBoundsException e) {
                log.error("", e);
                continue;
            }

            //存活再画出来
            if (demon.isAlive()) {
                BombMgr.instance.checkBong(demon, hero.bulletList);

                demon.drawSelf(g);
            } else {
                // TODO 去掉 延迟删除逻辑
//                enemyList.remove(demon);

                // 延迟删除 敌人和子弹
                if (demon.delayRemove) {
                    continue;
                }
                demon.delayRemove = true;

                DelayExecutor.addEvent(new AbstractDelayEvent(7_000) {
                    @Override
                    public void run() {
                        enemyList.remove(demon);
                    }
                });
            }
        }

        if (PlayStageMgr.instance.hasWinCurrentRound() || !hero.isAlive()) {
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
        } else if (e.getKeyCode() == KeyEvent.VK_T && !invokeNewStage) {
            invokeNewStage = true;
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

        if (re.getKeyCode() == KeyEvent.VK_T && invokeNewStage) {
            invokeNewStage = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        log.info("1={}", e);
    }

    /**
     * 创建砖
     */
    public void createB(List<Brick> bricks, int startX, int startY, int endX, int endY) {
        Brick template = new Brick(0, 0);
        for (int i = startX; i < endX; i += template.getWidth()) {
            for (int j = startY; j < endY; j += template.getHeight()) {
                Brick bs = new Brick(i, j);
                bricks.add(bs);
            }
        }
    }

    /**
     * 创建铁块
     */
    public void createI(List<Iron> irons, int startX, int startY, int endX, int endY) {
        Iron template = new Iron(0, 0);
        for (int i = startX; i < endX; i += template.getWidth()) {
            for (int j = startY; j < endY; j += template.getHeight()) {
                Iron bs = new Iron(i, j);
                irons.add(bs);
            }
        }
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