
package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.constant.StageCommand;
import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.Shot;
import com.github.kuangcp.tank.util.Audio;
import com.github.kuangcp.tank.util.ExecutePool;
import com.github.kuangcp.tank.util.Saved;
import com.github.kuangcp.tank.v3.MainFrame;
import com.github.kuangcp.tank.v3.PlayStageMgr;
import com.github.kuangcp.tank.v3.SettingFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用来放按钮的面板
 * 并且监控按钮事件
 */
@SuppressWarnings("serial")
@Slf4j
public class StageActionPanel extends JPanel implements ActionListener {
    static Thread actionThread = null;
    MainFrame frame;
    Hero hero;
    List<EnemyTank> ets;
    List<Iron> irons;
    List<Brick> bricks;
    int[][] ETS;
    int[] myself;
    Audio beginAudio;

    @Override
    public void actionPerformed(ActionEvent ae) {
        //上面是不合理的，会有隐藏的bug在里面，这种做法要抛弃

        if (ae.getActionCommand().equals(StageCommand.START)) {
            this.startNewStage();
        }

        if (ae.getActionCommand().equals("结束")) {
            System.out.println("结束");
            System.exit(0);
        }
        if (ae.getActionCommand().equals("暂停")) {
            System.out.println("暂停");
            frame.groundPanel.hero.setSpeed(0);
            Shot.setSpeed(0);
            for (EnemyTank et : ets) {
                et.setSpeed(0);
            }
            frame.requestFocus();
        }
        if (ae.getActionCommand().equals("继续")) {
            System.out.println("继续");
            frame.groundPanel.hero.setSpeed(3);
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
            SettingFrame.activeFocus(hero);
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
            TankGroundPanel.newStage = false;
            Shot.setSpeed(8);
            frame.remove(frame.jsp1);
//				Saved s = new Saved(ets, hero, bricks, irons,ETS,myself);
//				s.readAll();
            //实现一样的功能还省内存
            new Saved(ets, hero, bricks, irons, ETS, myself).readDataBase();

            EventQueue.invokeLater(frame);
//            actionThread = new Thread(frame);
//            actionThread.start();

            //读取
//			Saved s = new Saved(ets, hero, bricks, irons);
//			s.readAll();
            beginAudio = new Audio("./src/RE/GameBegin.wav");
//            beginAudio.start();
        }
    }

    private void startNewStage() {
        
        // 重新设置线程池大小
        final ThreadPoolExecutor pool = (ThreadPoolExecutor) ExecutePool.shotPool;
        final int poolSize = (int) Math.max(TankGroundPanel.getEnSize() * EnemyTank.maxLiveShot * 0.7, 10);
        pool.setCorePoolSize(poolSize);
        pool.setMaximumPoolSize(poolSize);

        if (Objects.nonNull(actionThread) && !actionThread.isInterrupted()) {
            PlayStageMgr.instance.markStopLogic();
            log.info("clean last stage");
            actionThread.interrupt();
            // TODO clean
            for (EnemyTank et : ets) {
                et.setAlive(false);
                et.setAbort(true);
            }
        }

        frame.firstStart = false;
        TankGroundPanel.newStage = true;
        Shot.setSpeed(8);
        frame.remove(frame.jsp1);

        log.info("start new stage frame thread");
        actionThread = new Thread(() -> {
            frame.run();
            if (beginAudio != null) {
                beginAudio.setLive(false);
            }
            beginAudio = new Audio("./src/RE/GameBegin.wav");
//        beginAudio.start();
            frame.groundPanel.startNewStage();
        });
        actionThread.setName("actionThread");
        actionThread.start();//将画板线程开启
    }

    public StageActionPanel(MainFrame frame, Hero he, java.util.List<EnemyTank> ets, java.util.List<Brick> bricks, List<Iron> irons, int[][] ETS, int[] myself) {
        this.frame = frame;
        this.hero = he;
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;
        this.ETS = ETS;
        this.myself = myself;
    }
}