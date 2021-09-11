
package com.github.kuangcp.tank.v3;


import com.github.kuangcp.tank.constant.StageCommand;
import com.github.kuangcp.tank.util.Audio;
import com.github.kuangcp.tank.util.Saved;
import com.github.kuangcp.tank.util.Setting;
import com.github.kuangcp.tank.v1.Brick;
import com.github.kuangcp.tank.v1.EnemyTank;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v1.Iron;
import com.github.kuangcp.tank.v2.Shot;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

/**
 * 用来放按钮的面板
 * 并且监控按钮事件
 */
@SuppressWarnings("serial")
@Slf4j
public class StageActionPanel extends JPanel implements ActionListener {
    static Thread actionThread = null;
    TankFrame frame;
    Hero hero;
    Vector<EnemyTank> ets;
    Vector<Iron> irons;
    Vector<Brick> bricks;
    int[][] ETS;
    int[] myself;
    Audio beginAudio;

    @Override
    public void actionPerformed(ActionEvent ae) {
        //上面是不合理的，会有隐藏的bug在里面，这种做法要抛弃
        if (ae.getActionCommand().equals(StageCommand.START)) {
            this.startGame();
        }

        if (ae.getActionCommand().equals("结束")) {
            System.out.println("结束");
            System.exit(0);
        }
        if (ae.getActionCommand().equals("暂停")) {
            System.out.println("暂停");
            frame.mp.hero.setSpeed(0);
            Shot.setSpeed(0);
            for (EnemyTank et : ets) {
                et.setSpeed(0);
            }
            frame.requestFocus();
        }
        if (ae.getActionCommand().equals("继续")) {
            System.out.println("继续");
            frame.mp.hero.setSpeed(3);
            Shot.setSpeed(8);
            for (EnemyTank et : ets) {
                et.setSpeed(2);
            }
            frame.requestFocus();//把焦点还给JFrame
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
            System.out.println(StageCommand.START);
            frame.firstStart = false;
            frame.mp.resumePlay = false;
            Shot.setSpeed(8);
            frame.remove(frame.jsp1);
//				Saved s = new Saved(ets, hero, bricks, irons,ETS,myself);
//				s.readAll();
            //实现一样的功能还省内存
            new Saved(ets, hero, bricks, irons, ETS, myself).readDataBase();
            actionThread = new Thread(frame);

            actionThread.start();//将画板线程开启
            //读取
//			Saved s = new Saved(ets, hero, bricks, irons);
//			s.readAll();
            beginAudio = new Audio("./src/RE/GameBegin.wav");
//            beginAudio.start();
        }
    }

    private void startGame() {
        if (Objects.nonNull(actionThread) && !actionThread.isInterrupted()) {
            log.info("clean last stage");
            actionThread.interrupt();
            // TODO clean
            for (EnemyTank et : ets) {
                et.setAlive(false);
            }
        }

        frame.firstStart = false;
        MyPanel3.resumePlay = true;
        Shot.setSpeed(8);
        frame.remove(frame.jsp1);
        actionThread = new Thread(frame);
        actionThread.setName("actionThread");
        actionThread.start();//将画板线程开启
        if (beginAudio != null) {
            beginAudio.setLive(false);
        }
        beginAudio = new Audio("./src/RE/GameBegin.wav");
//        beginAudio.start();
    }

    public StageActionPanel(TankFrame frame, Hero he, Vector<EnemyTank> ets, Vector<Brick> bricks, Vector<Iron> irons, int[][] ETS, int[] myself) {
        this.frame = frame;
        this.hero = he;
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;
        this.ETS = ETS;
        this.myself = myself;
    }
}