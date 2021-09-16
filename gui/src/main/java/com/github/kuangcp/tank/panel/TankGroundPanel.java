
package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.mgr.BombMgr;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
import com.github.kuangcp.tank.thread.ExitFlagRunnable;
import com.github.kuangcp.tank.util.KeyListener;
import com.github.kuangcp.tank.util.ListenEventGroup;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.AbstractDelayEvent;
import com.github.kuangcp.tank.util.executor.DelayExecutor;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import com.github.kuangcp.tank.v3.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * (20, 20, 720, 520) 该画板坦克运动区域
 */
@SuppressWarnings("serial")
@Slf4j
public class TankGroundPanel extends JPanel implements java.awt.event.KeyListener, ExitFlagRunnable {

    public volatile Hero hero;
    public KeyListener keyListener;
    public static boolean newStage = true;

    //定义一个 泛型的集合ets 表示敌人坦克集合
    public List<EnemyTank> enemyList = Collections.synchronizedList(new ArrayList<>());

    //定义砖块集合
    public List<Brick> bricks = Collections.synchronizedList(new ArrayList<>());
    public List<Iron> irons = Collections.synchronizedList(new ArrayList<>());

    //所有按下键的code集合
    public static int[][] enemyTankMap = new int[12][2];
    public static int[] myself = new int[6];

    private volatile boolean exit = false;

    public TankGroundPanel() {

    }

    public void exit() {
        this.exit = true;
    }

    /**
     * 画板的构造函数 用来初始化对象
     * 多个敌方坦克应该用集合类，而且要考虑线程安全所以不能用ArrayList只能用Vector
     */
    public void startNewRound() {
        //创建英雄坦克
        if (newStage) {//正常启动并创建坦克线程
            hero = new Hero(480, 500, 3);//坐标和步长和敌人坦克集合
            hero.setLife(10);//设置生命值
        } else {
            hero = new Hero(myself[0], myself[1], 3);
            hero.setLife(myself[2]);
            hero.setPrize(myself[3]);
        }

//        log.info("hero={}", hero);
        PlayStageMgr.init(hero, enemyList, bricks, irons);

        //多键监听实现
        keyListener = new KeyListener(ListenEventGroup.instance, hero, this);
        Thread p = new Thread(keyListener);
        p.setName("playerKeyEventListener");
        p.start();

        // 创建 敌人的坦克
        EnemyTank ett = null;
        if (newStage) {//正常启动并创建坦克线程
            for (int i = 0; i < PlayStageMgr.getEnemySize(); i++) {
                //在四个随机区域产生坦克
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), 2, i % 4);
                        ett.SetInfo(hero, enemyList, bricks, irons);
                        break;
                    case 1:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), 2, i % 4);
                        ett.SetInfo(hero, enemyList, bricks, irons);
                        break;
                    case 2:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), 2, i % 4);
                        ett.SetInfo(hero, enemyList, bricks, irons);
                        break;
                    case 3:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), 2, i % 4);
                        ett.SetInfo(hero, enemyList, bricks, irons);
                        break;
                }

                if (Objects.isNull(ett)) {
                    continue;
                }
                LoopEventExecutor.addLoopEvent(ett);
//                Thread t = new Thread(ett);
//                t.setName("enemyThread-" + ett.id);
//                t.start();
                //坦克加入集合
                enemyList.add(ett);
            }
        } else {
            /*进入读取文件步骤*/
            for (int i = 0; i < enemyTankMap.length; i++) {
                if (enemyTankMap[i][0] == 0) break;

                ett = new EnemyTank(enemyTankMap[i][0], enemyTankMap[i][1], 2, i % 4);
                ett.SetInfo(hero, enemyList, bricks, irons);

                LoopEventExecutor.addLoopEvent(ett);

//                Thread t = new Thread(ett);
//                t.setName("enemyThreadF-" + ett.id);
//                t.start();
                //坦克加入集合
                enemyList.add(ett);
//				System.out.print("创建单个坦克地址："+ett);
//				System.out.println("_____X="+ett.getX()+"Y="+ett.getY());

//				System.out.println("进入了读取数组这里");
            }
        }


        //创建砖块
//		createB(bricks, 40, 40, 200, 400);
//		createB(bricks, 200, 40, 400, 100);
//		createB(bricks, 400, 40, 700, 400);
//		createB(bricks, 200, 300, 400, 400);
//		createB(bricks, 40, 40, 700, 400);

        //眼睛
        createI(irons, 180, 100, 260, 180);
        createI(irons, 540, 100, 620, 180);
//		createB(bricks, 200, 120, 240, 160);
//		createB(bricks, 560, 120, 600, 160);

        //鼻子
        int m = 400, n = 260;
        createI(irons, m, n, m + 20, n + 10);
        createI(irons, m - 10, n + 10, m + 30, n + 20);
        createI(irons, m - 20, n + 20, m + 40, n + 30);
//		createB(bricks, 300, 240, 520, 360);
        //左右中间
//		createI(irons, 580, 370, 740, 380);
//		createI(irons, 20, 370, 210, 380);
        //左右下角
        createB(bricks, 20, 310, 300, 540);
        createB(bricks, 520, 310, 740, 540);
//		createI(irons, 250, 130, 300, 300);
//		createI(irons, 300, 130, 400, 200);
        //头像附近
        createB(bricks, 360, 460, 460, 480);
        createB(bricks, 360, 480, 380, 540);
        createB(bricks, 440, 460, 460, 540);

        createI(irons, 330, 410, 480, 430);

        PlayStageMgr.instance.markStartLogic();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (PlayStageMgr.waitStart() || Objects.isNull(hero)) {
            PlayStageMgr.drawStopIgnore(g, this);
            return;
        }

        /*画出坦克运动区域 */
        g.setColor(new Color(8, 8, 8, 237));
        g.fillRect(0, 0, 760, 560);//填满一个黑色矩形，说明了是一整个JPanel在JFrame上的
        g.setColor(Color.green);
        g.drawRect(20, 20, 720, 520);


        /*画出障碍物__砖__ 铁__*/

        g.setColor(new Color(200, 200, 200));
        for (int i = 0; i < irons.size(); i++) {
            Iron ir = irons.get(i);
            if (ir.getAlive()) {
                g.fill3DRect(ir.getHx(), ir.getHy(), 20, 10, false);
            } else {
                irons.remove(ir);
            }
        }

        g.setColor(new Color(190, 60, 50));
        for (int i = 0; i < bricks.size(); i++) {
            Brick bs = bricks.get(i);
            if (bs.getAlive()) {
                g.fill3DRect(bs.getHx(), bs.getHy(), 20, 10, false);
            } else {
                bricks.remove(bs);
            }
        }

        //眼睛
        g.setColor(Color.YELLOW);
        g.fillRect(200, 120, 40, 40);
        g.fillRect(560, 120, 40, 40);

        /*画出头像*/
        g.drawImage(AvatarImgMgr.instance.curImg, 380, 480,
                AvatarImgMgr.instance.width, AvatarImgMgr.instance.height, this);
        /*画出主坦克*/
        if (hero.isAlive()) {
            for (EnemyTank et : enemyList) {
                BombMgr.instance.checkBong(hero, et.bulletList);
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
        for (EnemyTank et : enemyList) {
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
                EnemyTank d = new EnemyTank(20 + (int) (Math.random() * 400), 20 + (int) (Math.random() * 300), 2, i % 4);
                d.SetInfo(hero, enemyList, bricks, irons);

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
            EnemyTank demon = enemyList.get(i);
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
     * 实现了墙
     * <p>
     * 当按下键盘上的键时监听者的处理函数
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (PlayStageMgr.pause) {
            return;
        }

        //加了if（内层的）限制后 实现了墙的限制（如果是游戏中的道具，该怎么办）
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            ListenEventGroup.instance.setShot(true);
        }

        if (ListenEventGroup.instance.hasPressMoveEvent()) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            ListenEventGroup.instance.setLeft(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            ListenEventGroup.instance.setRight(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            ListenEventGroup.instance.setUp(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            ListenEventGroup.instance.setDown(true);
        }

        //必须重新绘制窗口，不然上面的方法不能视觉上动起来
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent re) {
        ListenEventGroup.instance.handleRelease(re);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    /**
     * 创建 一块矩形的砖(20*10) 的函数
     */
    public void createB(List<Brick> bricks, int startX, int startY, int endX, int endY) {
        for (int i = startX; i < endX; i += 20) {
            for (int j = startY; j < endY; j += 10) {
                Brick bs = new Brick(i, j);
                bricks.add(bs);

            }
        }
    }

    /**
     * 创建铁块(20*10)
     */
    public void createI(List<Iron> irons, int startX, int startY, int endX, int endY) {
        for (int i = startX; i < endX; i += 20) {
            for (int j = startY; j < endY; j += 10) {
                Iron bs = new Iron(i, j);
                irons.add(bs);

            }
        }
    }

    //画板做成线程  画布进行刷新
    @Override
    public void run() {
        int maxFPS = 160;
        long fpsTime = (long) ((1000.0 / maxFPS) * 1000000);

        long now;
        //每隔100ms重绘
        while (!exit) {
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

        // clean listener
        if (Objects.nonNull(keyListener)) {
            keyListener.exit();
        }
    }
}