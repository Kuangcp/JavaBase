
package com.github.kuangcp.tank.v3;


import com.github.kuangcp.tank.v1.Brick;
import com.github.kuangcp.tank.v1.Demons;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v1.Iron;
import com.github.kuangcp.tank.v2.Shot;
import com.github.kuangcp.tank.util.Audio;
import com.github.kuangcp.tank.util.Saved;
import com.github.kuangcp.tank.util.Setting;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * 用来放按钮的面板
 * 并且监控按钮事件
 */
@SuppressWarnings("serial")
public class MyPanel301 extends JPanel implements ActionListener {


    static Thread t = null;
    TankFrame T;
    Hero hero;
    Vector<Demons> ets;
    Vector<Iron> irons;
    Vector<Brick> bricks;
    int[][] ETS;
    int[] myself;
    Audio Begin;

    @Override
    public void actionPerformed(ActionEvent ae) {
        //监听从按钮发来的事件


//		if(ae.getActionCommand().equals("开始")||ae.getActionCommand().equals("Continue")){
        //上面是不合理的，会有隐藏的bug在里面，这种做法要抛弃
        if (ae.getActionCommand().equals("开始")) {
            System.out.println("开始");
            T.count = 1;
            T.mp.Continue = true;
            Shot.setSpeed(8);
            T.remove(T.jsp1);
            t = new Thread(T);

            t.start();//将画板线程开启
            if (Begin != null) {
                Begin.setLive(false);
            }
            Begin = new Audio("./src/RE/GameBegin.wav");
            Begin.start();
        }
        if (ae.getActionCommand().equals("结束")) {
            System.out.println("结束");
            System.exit(0);
        }
        if (ae.getActionCommand().equals("暂停")) {
            System.out.println("暂停");
            T.mp.hero.setSpeed(0);
            Shot.setSpeed(0);
            for (int i = 0; i < ets.size(); i++) {
                ets.get(i).setSpeed(0);
            }
            T.requestFocus();
        }
        if (ae.getActionCommand().equals("继续")) {
            System.out.println("继续");
            T.mp.hero.setSpeed(3);
            Shot.setSpeed(8);
            for (int i = 0; i < ets.size(); i++) {
                ets.get(i).setSpeed(2);
            }
            T.requestFocus();//把焦点还给JFrame
        }
        if (ae.getActionCommand().equals("exit")) {
            //不能把下面的saveExit口令放到这里来或，会出现先后顺序的一些错误
            //口令与口令之间应该独立，不要有或，与这样的复杂逻辑结构
            System.out.println("退出游戏");
            System.exit(0);

        }
        if (ae.getActionCommand().equals("Help")) {
            Setting se = new Setting(hero);
        }
        if (ae.getActionCommand().equals("saveExit")) {
            System.out.println("按下了 保存并退出 按钮");
            Saved s = new Saved(ets, hero, bricks, irons, ETS, myself);
//			s.savedAll();
            s.saveDataBase();
            //退出
            System.out.println("已经退出游戏");
            System.exit(0);
        }
        if (ae.getActionCommand().equals("Continue")) {
            //重新开启一个画板
            System.out.println("开始");
            T.count = 1;
            T.mp.Continue = false;
            Shot.setSpeed(8);
            T.remove(T.jsp1);
//				Saved s = new Saved(ets, hero, bricks, irons,ETS,myself);
//				s.readAll();
            //实现一样的功能还省内存
            new Saved(ets, hero, bricks, irons, ETS, myself).readDataBase();
            t = new Thread(T);

            t.start();//将画板线程开启
            //读取
//			Saved s = new Saved(ets, hero, bricks, irons);
//			s.readAll();
            Begin = new Audio("./src/RE/GameBegin.wav");
            Begin.start();
        }
    }


    public MyPanel301(TankFrame T, Hero he, Vector<Demons> ets, Vector<Brick> bricks, Vector<Iron> irons, int[][] ETS, int[] myself) {
        this.T = T;
        this.hero = he;
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;
        this.ETS = ETS;
        this.myself = myself;
//		System.out.println("MyPanel301的成员地址"+ets);
    }
}
//class P extends JPanel{
//	Image over = null;
//	public P(){
//		try {
//			over = ImageIO.read(getClass().getResource("/images/Over4.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void paint(Graphics g){
//		super.paint(g);
//		
//		g.drawImage(over,0,0,760,560,this);
//
//	}
//}
