package com.github.kuangcp.tank.v1;
/**
 * 1 绘画出坦克 并且能做到对它的移动操作
 * 2 移动最好封装成函数（面向对象思想）这是坦克的行为属性 可扩展
 * 3 加上对坦克运动的边界限定
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

@SuppressWarnings("serial")
public class TankGame1 extends JFrame {


    MyPanel mp = null;

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TankGame1 Tank = new TankGame1();
    }

    //最外层JFrame的构造函数
    public TankGame1() {


        mp = new MyPanel();

        this.add(mp);
        //注册键盘监听
        //下面的语句翻译为 ：当前类的监；听者是mp
        this.addKeyListener(mp);

        this.setLocation(900, 200);
        this.setSize(500, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

@SuppressWarnings("serial")
/////////////////////////////////////////////////////////////////////////////
class MyPanel extends JPanel implements KeyListener {

    //定义我的坦克
    Hero hero = null;
    int enSize = 3;  //敌人的数量
    //定义泛型的集合类
    Vector<Demons> ets = new Vector<Demons>();
    Vector<Brick> bricks = new Vector<Brick>();

    //画板的构造函数 放图形的对象
    public MyPanel() {
        //多个敌方坦克应该用集合类，而且要考虑线程安全所以不能用ArrayList只能用Vector
        //实例化英雄坦克
//		hero = new Hero(10,200,10,ets,bricks);//坐标和步长


        //初始化敌人的坦克
		/*for (int i=0;i<enSize;i++){
			//创建敌人坦克 再加入到集合里去
			Demons et = new Demons((i+1)*50,20 ,0 ,1,hero);
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
        //画出子弹
        if (hero.s != null) {
            g.draw3DRect(hero.s.sx, hero.s.sy, 1, 1, false);
        }

        //画出敌人坦克
        for (Demons s : ets) {
            this.drawTank(s.getX(), s.getY(), g, s.getDirect(), s.getType());
        }

        //		画出砖块
        g.setColor(Color.yellow);
        g.drawRect(0, 0, 400, 300);
        for (int j = 0; j < 20; j++) {
            g.draw3DRect(60 + 8 * j, 50, 5, 10, false);
        }

    }

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
        //加了if条件后 实现了墙的限制（如果是游戏中的道具，该怎么办）
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

        //必须重新绘制窗口，不然上面的方法不能视觉上动起来
        this.repaint();

    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}
/*
//为了能一边走一边发子弹而设计的专门处理按下j键子弹发射的类
class JS extends JPanel implements KeyListener{

	Hero hero=null;
	public JS(){}
	public JS(Hero h){
		hero = h;
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == KeyEvent.VK_J){
			//开火
			hero.shotEnemy();
		}
		
		//必须重新绘制窗口，不然上面的方法不能视觉上动起来
		this.repaint();
	
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
*/