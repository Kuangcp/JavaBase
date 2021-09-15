package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.thread.ExitFlagRunnable;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.v3.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 就是一个用来显示属性的画板
 * JLabel 被add后 就算他发生改变，对之前已经add了的组件 不会有影响
 * 但是可以用setText来进行改变
 */
@Slf4j
@SuppressWarnings("serial")
public class HeroInfoPanel extends JPanel implements ExitFlagRunnable {

    JLabel jl1;
    JLabel prizeNo;
    List<EnemyTank> ets;

    final Tank heroIcon = new Tank(20, 20, 0) {
        @Override
        public void drawSelf(Graphics g) {
            g.setColor(Color.yellow);
            super.drawSelf(g);
        }

        @Override
        public void run() {

        }
    };
    final Tank enemyIcon = new Tank(100, 20, 0) {
        @Override
        public void drawSelf(Graphics g) {
            g.setColor(Color.cyan);
            super.drawSelf(g);
        }

        @Override
        public void run() {

        }
    };

    private volatile boolean exit = false;

    public void exit() {
        this.exit = true;
    }

    public HeroInfoPanel(JLabel jl1, List<EnemyTank> ets, JLabel prizeNo) {
        this.jl1 = jl1;
        this.ets = ets;
        this.prizeNo = prizeNo;
//		jl1 =new JLabel("生命值： "+Hero.Life/3);
//		MyPanel302 mp = new MyPanel302();
//		mp.add(jl1,BorderLayout.SOUTH);
    }

    public void paint(Graphics g) {
        super.paint(g);

        heroIcon.drawSelf(g);
        enemyIcon.drawSelf(g);
    }

    public void run() {
        while (!exit) {
            TankTool.yieldMsTime(500);
            if (PlayStageMgr.waitStart()) {
                continue;
            }
            /*
             * 如果是下面的方法的话，来不及设置成0 就退出线程了，所以把设置0放在了退出线程那里更好
             */
//			if(!hero.getisLive()){
//				jl1.setText("生命值：   0");
//			}else {
//				jl1.setText("生命值：  "+hero.getLife());
//			}

            if (PlayStageMgr.instance.hero.isAlive()) {//生命值的自动更改
                jl1.setText("           ：" + PlayStageMgr.instance.hero.getLife() + "                  ：  " + ets.size());
            } else {
                jl1.setText("           ：0" + "                  :  " + ets.size());
            }
            prizeNo.setText("战绩：" + PlayStageMgr.instance.hero.getPrize());
//			TankGame3.mp3.add(TankGame3.jl1 );
//			System.out.println("画板执行了");
            repaint();

            //不加的话线程没有退出
//			if(hero.getLife() <= 0){
//			    jl1.setText("           ：0"+"                  :  "+ets.size());
//				break;//退出线程
//			}
        }
    }

}
