package com.github.kuangcp.tank.backup.v2;

import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.domain.StageBorder;

import javax.swing.*;
import java.util.Collections;

/**
 * 1 绘画出坦克 并且能做到对它的移动操作
 * 2 移动最好封装成函数（面向对象思想）这是坦克的行为属性 可扩展
 * 3 加上对坦克运动的边界限定
 */
@SuppressWarnings("serial")
public class TankGameV2 extends JFrame {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        TankGameV2 Tank = new TankGameV2();
    }

    //最外层JFrame的构造函数
    public TankGameV2() {
        MonitorExecutor.init();
        RoundMapMgr.init();
        PlayStageMgr.init(new Hero(50, 20, 5), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        MainPanelV2 panel = new MainPanelV2();
        this.add(panel);
        this.addKeyListener(panel);
        final Thread panelThread = new Thread(panel);
        panelThread.start();


        final StageBorder border = RoundMapMgr.instance.border;

        this.setLocation(200, 50);
        this.setSize(border.getMaxX() + border.getMinX(), border.getMaxY() + border.getMinY() * 2);
        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}