package com.github.kuangcp.tank.v1;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author https://github.com/kuangcp on 2021-09-11 23:15
 */
@SuppressWarnings("serial")
class MyPanel extends JPanel implements KeyListener {

    //定义我的坦克
    Hero hero = null;
    int enSize = 3;  //敌人的数量
    //定义泛型的集合类
    Vector<EnemyTank> ets = new Vector<>();
    Vector<Brick> bricks = new Vector<>();

    //画板的构造函数 放图形的对象
    public MyPanel() {
    }

    //重写paint
    public void paint(Graphics g) {
        super.paint(g);
        hero = new Hero(20,20,4,new Vector<>(), new Vector<>(), new Vector<>());
        g.fillRect(0, 0, 500, 400);

        //调用函数绘画出主坦克
        this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getType());
        //画出子弹
        if (hero.s != null) {
            g.draw3DRect(hero.s.sx, hero.s.sy, 1, 1, false);
        }

        //画出敌人坦克
        for (EnemyTank s : ets) {
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
            hero.setDirect(2);
            if ((hero.getX() - 10) > 0)
                hero.moveleft();

        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(3);
            if ((hero.getX() + 15) < 405)
                hero.moveright();
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if ((hero.getY() - 13) > 0)
                hero.moveup();

        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(1);
            if ((hero.getY() - 15) < 275)
                hero.movedown();

        }
        //必须重新绘制窗口，不然上面的方法不能视觉上动起来
        this.repaint();
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}
