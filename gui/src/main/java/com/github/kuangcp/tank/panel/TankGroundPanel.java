
package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.util.ExecutePool;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.Shot;
import com.github.kuangcp.tank.util.ListenEventGroup;
import com.github.kuangcp.tank.util.KeyListener;
import com.github.kuangcp.tank.resource.AvatarMgr;
import com.github.kuangcp.tank.resource.BombMgr;
import com.github.kuangcp.tank.thread.ExitFlagRunnable;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * (20, 20, 720, 520) 该画板坦克运动区域
 */
@SuppressWarnings("serial")
@Slf4j
public class TankGroundPanel extends JPanel implements java.awt.event.KeyListener, ExitFlagRunnable {

    public Hero hero;
    public KeyListener keyListener;
    public static boolean newStage = true;
    // 敌人的数量
    public static int enSize = 10;
    //定义一个 泛型的集合ets 表示敌人坦克集合
    public Vector<EnemyTank> enemyList = new Vector<>();

    //定义砖块集合
    public Vector<Brick> bricks = new Vector<>();
    public Vector<Iron> irons = new Vector<>();
    //所有按下键的code集合
    public static int[][] enemyTankMap = new int[12][2];
    public static int[] myself = new int[6];
    private static final ExecutorService delayPool = ExecutePool.buildFixedPool("enemyDelayRemove", 3);

    Image overImg = null;
    Image winImg = null;

    private volatile boolean exit = false;

    public void exit() {
        this.exit = true;
    }

    /**
     * 画板的构造函数 用来初始化对象
     * 多个敌方坦克应该用集合类，而且要考虑线程安全所以不能用ArrayList只能用Vector
     */
    public TankGroundPanel() {
        //创建英雄坦克
        if (newStage) {//正常启动并创建坦克线程
            hero = new Hero(480, 500, 3, enemyList, bricks, irons);//坐标和步长和敌人坦克集合
            hero.setLife(10);//设置生命值
        } else {
            hero = new Hero(myself[0], myself[1], 3, enemyList, bricks, irons);
            hero.setLife(myself[2]);
            hero.setPrize(myself[3]);
        }

        //多键监听实现
        keyListener = new KeyListener(ListenEventGroup.instance, hero, this);
        Thread p = new Thread(keyListener);
        p.setName("keyEventGroup");
        p.start();

        // 创建 敌人的坦克
        EnemyTank ett = null;
        if (newStage) {//正常启动并创建坦克线程
            for (int i = 0; i < enSize; i++) {
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
                Thread t = new Thread(ett);
                t.setName("enemyThread-" + ett.id);
                t.start();
                //坦克加入集合
                enemyList.add(ett);
            }
        } else {
            /*进入读取文件步骤*/
            for (int i = 0; i < enemyTankMap.length; i++) {
                if (enemyTankMap[i][0] == 0) break;

                ett = new EnemyTank(enemyTankMap[i][0], enemyTankMap[i][1], 2, i % 4);
                ett.SetInfo(hero, enemyList, bricks, irons);
                Thread t = new Thread(ett);
                t.setName("enemyThreadF-" + ett.id);
                t.start();
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

        try {
            BombMgr.instance.initImg();

            overImg = ImageIO.read(getClass().getResourceAsStream("/images/Over4.jpg"));

            AvatarMgr.instance.initImg();
            winImg = ImageIO.read(getClass().getResourceAsStream("/images/Win2.jpg"));
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Override
    public void paint(Graphics g) {
        /*画出坦克运动区域 */
        super.paint(g);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, 760, 560);//填满一个黑色矩形，说明了是一整个JPanel在JFrame上的
        g.setColor(Color.green);
        g.drawRect(20, 20, 720, 520);
        Shot myShot;

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

        g.drawImage(AvatarMgr.instance.curImg, 380, 480, 60, 60, this);
        /*画出主坦克*/
        if (hero.isAlive()) {
            for (EnemyTank et : enemyList) {
                BombMgr.instance.checkBong(hero, et.ds);
            }

            this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getType());
        }

        /*画出自己的子弹*/  //画子弹是可以封装成一个方法的
        //		从ss 这个子弹集合中 取出每颗子弹，并画出来
        for (int i = 0; i < hero.shotList.size(); i++) {
            myShot = hero.shotList.get(i);
            for (Brick brick : bricks) {
                TankTool.judgeHint(myShot, brick);
            }
            for (Iron iron : irons) {
                TankTool.judgeHint(myShot, iron);
            }
            if (myShot.sx < 440 && myShot.sx > 380 && myShot.sy < 540 && myShot.sy > 480) {
                myShot.isLive = false;
                hero.setAlive(false);
            }
            if (hero.shotList.get(i) != null && hero.shotList.get(i).isLive) {
                g.draw3DRect(myShot.sx, myShot.sy, 1, 1, false);
            }

            //子弹线程死了 就要把它从集合中删除
            if (!myShot.isLive) {
                hero.shotList.remove(myShot);
            }
        }

        /*敌人子弹*/
        for (EnemyTank et : enemyList) {
            for (int i = 0; i < et.ds.size(); i++) {
                myShot = et.ds.get(i);
                for (Brick brick : bricks) {
                    TankTool.judgeHint(myShot, brick);
                }
                for (Iron iron : irons) {
                    TankTool.judgeHint(myShot, iron);
                }
                if (myShot.sx < 440 && myShot.sx > 380 && myShot.sy < 540 && myShot.sy > 480) {
                    myShot.isLive = false;
                    hero.setAlive(false);
                }
                if (et.ds.get(i) != null && et.ds.get(i).isLive) {
                    g.setColor(Color.cyan);
                    g.draw3DRect(myShot.sx, myShot.sy, 1, 1, false);

                }
                if (!myShot.isLive) {
                    et.ds.remove(myShot);
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
                Thread fillThread = new Thread(d);
                fillThread.setName("fillEnemy" + d.id);
                fillThread.start();
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
                //坦克间的碰撞
//				for(int k=0;k<ets.size();k++){
////					demon.setWith(true);
//					if(!Tool.Rush(demon, ets.get(k))){
//						demon.setWith(false);
//					}
////					else{
//					demon.setWith(true);
////					}
//				}
//				for(int t=0;t<bricks.size();t++){
//				    demon.setBri(true);
//				    if(!Tool.XHinder(demon, bricks.get(t))){
//					   demon.setWith(false);
//					}
//				    demon.setWith(true);
//				}

                //坦克和障碍物
//				int count =0;
//				for(int t=0;t<bricks.size();t++){
////					demon.setBri(true);
//					if(!Tool.XHinder(demon, bricks.get(t))){
////						demon.setBri(false);
//						count++;
////						demon.setDirect(3-demon.getDirect());
////						System.out.println("检测");
//					}
////					demon.setBri(true);
////					if(count ==1){
////					if(!demon.isBri()){
//////						demon.setDirect(3-demon.getDirect());
////						demon.setBri(true);
////					}
////					Timer timer = new Timer();
////			        timer.schedule(new TimerTask() {
////			            public void run() {
////			                System.out.println("-------设定要指定任务--------");
////			                demon.setBri(true);
////			            }
////			        }, 0, 300);
//////					
////				} 
//				demon.setBri(true);

                BombMgr.instance.checkBong(demon, hero.shotList);

                this.drawTank(demon.getX(), demon.getY(), g, demon.getDirect(), demon.getType());
            } else {
                // 延迟删除 敌人和子弹
                if (demon.delayRemove) {
                    continue;
                }
                demon.delayRemove = true;
                delayPool.execute(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(8);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                    enemyList.remove(demon);
                    demon.cleanResource();
                });
            }
        }

        //游戏结束的画面
        if (!hero.isAlive()) {
            g.drawImage(overImg, 0, 0, 760, 560, this);
            g.drawString("您的总成绩为：" + hero.getPrize(), 320, 500);
        }

        //判断获胜
        if (hero.getPrize() >= 40) {
            g.drawImage(winImg, 0, 0, 760, 560, this);
            g.drawString("您的总成绩为：" + hero.getPrize(), 320, 500);
        }
    }

    /**
     * 画出所有坦克的函数 XY是坦克中心的坐标，不是画图参照点
     */
    public void drawTank(int X, int Y, Graphics g, int direct, int type) {

        //判断坦克类型
        //系统画图函数的参照点
        int x, y;
        switch (type) {
            case 1:
                g.setColor(Color.cyan);
                break;
            case 0:
                g.setColor(Color.yellow);
                hero.g = g;

                break;
        }

        //判断方向
        switch (direct) {//上下左右

            case 0: {//向上VK_UP

                x = X - 10;
                y = Y - 15;//把坦克中心坐标换算成系统用来画坦克的坐标
                //（并且全是取的左上角）

                //1.左边的矩形
                g.fill3DRect(x, y, 5, 30, false);
                //2.画出右边矩形
                g.fill3DRect(x + 15, y, 5, 30, false);
                //3.画出中间矩形
                g.fill3DRect(x + 5, y + 5, 10, 20, false);
                //4.画出圆形
                g.fillOval(x + 4, y + 10, 10, 10);
                //5.画出线
                g.drawLine(x + 9, y + 15, x + 9, y);
//				for(int i=0;i<10;i++){
////					g.setColor(Color.BLACK);
//					g.fill3DRect(x,y+3*i, 5, 2,false);
//				}
//				for(int i=0;i<10;i++){
////					g.setColor(Color.BLACK);
//					g.fill3DRect(x+15,y+3*i, 5, 2,false);
//				}
////				g.setColor(Color.yellow);
                break;
            }
            case 1: {//向下 VK_DOWN
                x = X - 10;
                y = Y - 15;
                g.fill3DRect(x, y, 5, 30, false);
                g.fill3DRect(x + 15, y, 5, 30, false);
                g.fill3DRect(x + 5, y + 5, 10, 20, false);
                g.fillOval(x + 4, y + 10, 10, 10);
                g.drawLine(x + 9, y + 15, x + 9, y + 30);
                break;
            }
            case 2: {//向左 VK_LEFT
                x = X - 15;
                y = Y - 10;
                g.fill3DRect(x, y, 30, 5, false);
                g.fill3DRect(x, y + 15, 30, 5, false);
                g.fill3DRect(x + 5, y + 5, 20, 10, false);
                g.fillOval(x + 10, y + 4, 10, 10);
                g.drawLine(x + 15, y + 9, x, y + 9);
                break;
            }
            case 3: {//向右VK_RIGHT
                x = X - 15;
                y = Y - 10;
                g.fill3DRect(x, y, 30, 5, false);
                g.fill3DRect(x, y + 15, 30, 5, false);
                g.fill3DRect(x + 5, y + 5, 20, 10, false);
                g.fillOval(x + 10, y + 4, 10, 10);
                g.drawLine(x + 15, y + 9, x + 30, y + 9);
                break;
            }
        }
    }

    /**
     * 实现了墙
     * <p>
     * 当按下键盘上的键时监听者的处理函数
     */
    @Override
    public void keyPressed(KeyEvent e) {
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
    public void createB(Vector<Brick> bricks, int startX, int startY, int endX, int endY) {
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
    public void createI(Vector<Iron> irons, int startX, int startY, int endX, int endY) {
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

        long now = System.nanoTime();
        //每隔100ms重绘
        while (!exit) {
            now = System.nanoTime();
//            try {
//                Thread.sleep(10);
//                //里面的数字就是刷新时间的间隔 而且每当按下J键就会拉起MyPanel线程 相当于加快了刷新
//            } catch (Exception e) {
//                log.error("", e);
//            }

            // 进行重绘
            this.repaint();
            final long waste = System.nanoTime() - now;
            if (waste > fpsTime) {
                continue;
            }

            TankTool.yieldTime(fpsTime - waste, TimeUnit.NANOSECONDS);
            if (!hero.isAlive()) {
                break;
            }
        }

        // clean listener
        keyListener.exit();
    }

    public static int getEnSize() {
        return enSize;
    }

    public static void setEnSize(int enSize) {
        TankGroundPanel.enSize = enSize;
    }
}