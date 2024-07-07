
package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.constant.ButtonCommand;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.util.Audio;
import com.github.kuangcp.tank.frame.MainFrame;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * 用来放按钮的面板
 * 并且监控按钮事件
 */
@Slf4j
public class StageActionPanel extends JPanel implements ActionListener {

    private static Thread actionThread = null;
    MainFrame frame;
    Audio beginAudio;

    @Override
    public void actionPerformed(ActionEvent ae) {
        // TODO 删除无用代码
        if (ae.getActionCommand().equals(ButtonCommand.START)) {
            this.startNewStage();
        } else if (ae.getActionCommand().equals(ButtonCommand.EXIT)) {
            System.out.println("结束");
            System.exit(0);
        } else if (ae.getActionCommand().equals(ButtonCommand.PAUSE)) {
            System.out.println("暂停");
            PlayStageMgr.pause = true;
            frame.requestFocus();
        } else if (ae.getActionCommand().equals(ButtonCommand.RESUME)) {
            System.out.println("继续");
            PlayStageMgr.pause = false;
            frame.requestFocus();
        } else if (ae.getActionCommand().equals(ButtonCommand.ABORT)) {
            //不能把下面的saveExit口令放到这里来或，会出现先后顺序的一些错误
            //口令与口令之间应该独立，不要有或，与这样的复杂逻辑结构
            System.out.println("退出游戏");
            System.exit(0);
        } else if (ae.getActionCommand().equals("saveExit")) {
            System.out.println("按下了 保存并退出 按钮");
            //退出
            System.out.println("已经退出游戏");
            System.exit(0);
        } else if (ae.getActionCommand().equals("Continue")) {
            //重新开启一个画板
            System.out.println(ButtonCommand.START);
            frame.firstStart = false;
            TankGroundPanel.newStage = false;
            Bullet.setSpeed(8);
//            frame.remove(frame.centerPanel);

            EventQueue.invokeLater(frame);
//            actionThread = new Thread(frame);
//            actionThread.start();

            //读取
            beginAudio = new Audio("./src/RE/GameBegin.wav");
//            beginAudio.start();
        }
    }

    public void startNewStage() {
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
            actionThread.interrupt();
            PlayStageMgr.instance.abortStage();
        }

        frame.firstStart = false;
        TankGroundPanel.newStage = true;
        Bullet.setSpeed(8);
        frame.remove(frame.getContentPane());

//        log.info("start new stage frame thread");
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

    public StageActionPanel(MainFrame frame) {
        this.frame = frame;
    }
}