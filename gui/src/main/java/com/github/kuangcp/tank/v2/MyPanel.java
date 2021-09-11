package com.github.kuangcp.tank.v2;


import com.github.kuangcp.tank.v1.Brick;
import com.github.kuangcp.tank.v1.EnemyTank;
import com.github.kuangcp.tank.v1.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;


@SuppressWarnings("serial")
public class MyPanel extends JPanel implements KeyListener, Runnable {

    //定义我的坦克
    Hero hero = null;
    int enSize = 3;  // 敌人的数量
    //定义一个 泛型的集合类 表示敌人坦克集合
    Vector<EnemyTank> ets = new Vector<EnemyTank>();
    Vector<Brick> bricks = new Vector<Brick>();


    //画板的构造函数 放图形的对象
    public MyPanel() {
        //多个敌方坦克应该用集合类，而且要考虑线程安全所以不能用ArrayList只能用Vector
        //实例化英雄坦克
//		hero = new Hero(10,200,10,ets,bricks);//坐标和步长
//		JS j = new JS(hero);


        //初始化敌人的坦克
		/*for (int i=0;i<enSize;i++){
			//创建敌人坦克 再加入到集合里去
			Demons et = new Demons((i+1)*50,20 ,0,1,hero );
			et.setDirect(1);
			//加入
			ets.add(et); 
		}*/

    }

    //重写paint
    public void paint(Graphics g) {

        super.paint(g);
        g.fillRect(0, 0, 500, 400);

//调用函数绘画出主坦克
        this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getType());


//		从ss 这个子弹集合中 取出每颗子弹，并画出来
        for (int i = 0; i < this.hero.ss.size(); i++) {
            Shot myShot = hero.ss.get(i);
            //  以下代码只能画出一颗子弹 不完善
            if (myShot != null) {
                g.draw3DRect(hero.s.sx, hero.s.sy, 1, 1, false);
            }
        }


        //自己做的函数 完全跑不起来
		/*
		if (hero.s!=null ){//&& e.getKeyCode()==KeyEvent.VK_J
			this.drawS(hero.s.sx, hero.s.sy,hero.getDirect(),g);
			g.draw3DRect(hero.s.sx, hero.s.sy, 1, 1, false);
		}*/


        //画出敌人坦克
        for (int i = 0; i < ets.size(); i++) {
            EnemyTank s = ets.get(i);
            this.drawTank(s.getX(), s.getY(), g, s.getDirect(), s.getType());
        }

        //		画出砖块
        g.setColor(Color.yellow);
        g.drawRect(0, 0, 400, 300);
        for (int j = 0; j < 20; j++) {
            g.draw3DRect(60 + 8 * j, 50, 5, 10, false);
        }

    }


    /*//画出会动的子弹的函数  自己
        public void drawS(int sx,int sy,int direct,Graphics g){
            switch (direct){//上下左右
            case 0:
                while (sy>0){
                    g.draw3DRect(sx, sy, 1, 1, false);
                    sy-=3;
                    repaint();
                }
                break;
            case 1:
                while (sy<300){
                    g.draw3DRect(sx, sy, 1, 1, false);
                    sy+=3;
                    repaint();
                }
                break;
            case 2:
                while (sx>0){
                    g.draw3DRect(sx, sy, 1, 1, false);
                    sx-=3;
                    repaint();
                }
                break;
            case 3:
                while (sx<400){
                    g.draw3DRect(sx, sy, 1, 1, false);
                    sx+=3;
                    repaint();
                }
                break;
            }
            repaint();
        }
    */
    //画出坦克的函数 XY是坦克中心的坐标，不是画图参照点
    public void drawTank(int X, int Y, Graphics g, int direct, int type) {
        //判断坦克类型
        int x, y;//系统画图函数的参照点
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
        switch (direct) {

            case 0: {//向上VK_UP
                x = X - 10;
                y = Y - 15;
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

    //当按下键盘上的键时监听者的处理函数
    public void keyPressed(KeyEvent e) {
//		加了if条件后 实现了墙的限制（如果是游戏中的道具，该怎么办）

        if (e.getKeyCode() == KeyEvent.VK_A) {
            //			System.out.print("左");
            hero.setDirect(2);
            if ((hero.getX() - 10) > 0)
                hero.moveleft();

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            //			System.out.print("右");
            hero.setDirect(3);
            if ((hero.getX() + 15) < 405)
                hero.moveright();
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            //			System.out.print("上");
            hero.setDirect(0);
            if ((hero.getY() - 13) > 0)
                hero.moveup();

        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            //			System.out.print("下");
            hero.setDirect(1);
            if ((hero.getY() - 15) < 275)
                hero.movedown();

        }
        this.repaint();
//			判断玩家是否按下 J键   //开火
        if (e.getKeyChar() == KeyEvent.VK_J) {
            System.out.println("按下 开火键");
//			自己做的开火函数
//				hero.drawS(hero.getX(), hero.getY(), hero.getDirect(), hero.g);
            hero.shotEnemy();
            repaint();
        }


        //必须重新绘制窗口，不然上面的方法不能视觉上动起来
        this.repaint();

    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    //画板做成线程  画布进行刷新
    public void run() {
        // TODO Auto-generated method stub
        //每隔100ms重绘
        while (true) {

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            //单纯是为了测试，不需要这句话来耗资源
//			System.out.println("Panel的一个线程睡觉");
//			进行重绘
            this.repaint();
        }

    }
}