/**
 * 就是一个用来显示属性的画板
 * 不停的刷新
 * JLabel 被add后 就算他发生改变，对之前已经add了的组件 不会有影响
 * 但是可以用setText来进行改变
 */
package com.github.kuangcp.tank.v3;


import com.github.kuangcp.tank.v1.EnemyTank;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v3.thread.ExitFlagRunnable;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class MyPanel302 extends JPanel implements ExitFlagRunnable {

    JLabel jl1;
    JLabel prizeNo;
    MyPanel302 mp = null;
    Hero hero;
    Vector<EnemyTank> ets;

    private volatile boolean exit = false;

    public void exit() {
        this.exit = true;
    }

    public MyPanel302(JLabel jl1, Hero hero, Vector<EnemyTank> ets, JLabel prizeNo) {
        this.jl1 = jl1;
        this.hero = hero;
        this.ets = ets;
        this.prizeNo = prizeNo;
//		jl1 =new JLabel("生命值： "+Hero.Life/3);
//		MyPanel302 mp = new MyPanel302();
//		mp.add(jl1,BorderLayout.SOUTH);
    }

    public void paint(Graphics g) {
        super.paint(g);

        int X = 20, Y = 20;
        int x, y;
/**主坦克*/
        g.setColor(Color.yellow);
        //向上
        x = X - 10;
        y = Y - 15;//把坦克中心坐标换算成系统用来画坦克的坐标
        //（并且全是取的左上角）

        g.fill3DRect(x, y, 5, 30, false);
        g.fill3DRect(x + 15, y, 5, 30, false);
        g.fill3DRect(x + 5, y + 5, 10, 20, false);
        g.fillOval(x + 4, y + 10, 10, 10);
        g.drawLine(x + 9, y + 15, x + 9, y);

/**敌人坦克*/
        g.setColor(Color.cyan);
        X = 100;
        Y = 20;
        x = X - 10;
        y = Y - 15;//把坦克中心坐标换算成系统用来画坦克的坐标
        //（并且全是取的左上角）

        g.fill3DRect(x, y, 5, 30, false);
        g.fill3DRect(x + 15, y, 5, 30, false);
        g.fill3DRect(x + 5, y + 5, 10, 20, false);
        g.fillOval(x + 4, y + 10, 10, 10);
        g.drawLine(x + 9, y + 15, x + 9, y);

//		MyPanel3 tank = new MyPanel3();//因为原本的MyPanel是做成了线程  莫名其妙就被调用到了
//		//主坦克：
//		g.setColor(Color.yellow);
//		tank.drawTank(0, 0, g, 0, 0);
//		//敌人坦克：
//		g.setColor(Color.cyan);
//		tank.drawTank(0, 40, g, 0, 1);
    }

    public void run() {
        while (!exit) {
            /*
             * 如果是下面的方法的话，来不及设置成0 就退出线程了，所以把设置0放在了退出线程那里更好
             */
//			if(!hero.getisLive()){
//				jl1.setText("生命值：   0");
//			}else {
//				jl1.setText("生命值：  "+hero.getLife());
//			}

            if (hero.isAlive()) {//生命值的自动更改
                jl1.setText("           ：" + hero.getLife() + "                  ：  " + ets.size());
            } else {
                jl1.setText("           ：0" + "                  :  " + ets.size());
            }
            prizeNo.setText("战绩：" + hero.getPrize());
//			TankGame3.mp3.add(TankGame3.jl1 );
//			System.out.println("画板执行了");
            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //不加的话线程没有退出
//			if(hero.getLife() <= 0){
//			    jl1.setText("           ：0"+"                  :  "+ets.size());
//				break;//退出线程
//			}
        }
    }

}
