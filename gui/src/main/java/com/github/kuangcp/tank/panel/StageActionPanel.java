
package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.constant.ButtonCommand;
import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.util.Audio;
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

        if (ae.getActionCommand().equals(ButtonCommand.START)) {
            this.startNewStage();
        }

        if (ae.getActionCommand().equals(ButtonCommand.EXIT)) {
            System.out.println("结束");
            System.exit(0);
        }

        if (ae.getActionCommand().equals(ButtonCommand.PAUSE)) {
            System.out.println("暂停");
            PlayStageMgr.pause = true;
            frame.requestFocus();
        }

        if (ae.getActionCommand().equals(ButtonCommand.RESUME)) {
            System.out.println("继续");
            PlayStageMgr.pause = false;
            frame.requestFocus();
        }

        if (ae.getActionCommand().equals(ButtonCommand.ABORT)) {
            //不能把下面的saveExit口令放到这里来或，会出现先后顺序的一些错误
            //口令与口令之间应该独立，不要有或，与这样的复杂逻辑结构
            System.out.println("退出游戏");
            System.exit(0);

        }

        if (ae.getActionCommand().equals(ButtonCommand.SETTING_FRAME)) {
            SettingFrame.activeFocus();
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
            System.out.println(ButtonCommand.START);
            frame.firstStart = false;
            TankGroundPanel.newStage = false;
            Bullet.setSpeed(8);
            frame.remove(frame.centerPanel);
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

        final int totalMaxShots = PlayStageMgr.getEnemySize() * EnemyTank.maxLiveShot;

        // 重新设置线程池大小
//        final ThreadPoolExecutor pool = (ThreadPoolExecutor) ExecutePool.shotPool;
//        final int poolSize = (int) Math.max(PlayStageMgr.getEnemySize() * EnemyTank.maxLiveShot * 0.6, 10);
//        pool.setCorePoolSize(poolSize);
//        pool.setMaximumPoolSize(poolSize);

        // 协程
//        final int poolSize = (int) Math.max(totalMaxShots * 0.4, 10);
//        ExecutePool.shotScheduler.getForkJoinPool().shutdownNow();
//        ExecutePool.shotScheduler = new FiberForkJoinScheduler("enemyShot", poolSize, null, false);

        if (Objects.nonNull(actionThread) && !actionThread.isInterrupted()) {
            log.info("clean last stage");
            actionThread.interrupt();
            PlayStageMgr.instance.abortStage();
        }

        frame.firstStart = false;
        TankGroundPanel.newStage = true;
        Bullet.setSpeed(8);
        frame.remove(frame.centerPanel);

        log.info("start new stage frame thread, shot:{}", totalMaxShots);
        actionThread = new Thread(() -> {
            frame.run();
            if (beginAudio != null) {
                beginAudio.setLive(false);
            }
            beginAudio = new Audio("./src/RE/GameBegin.wav");
//        beginAudio.start();
            frame.groundPanel.startNewRound();
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