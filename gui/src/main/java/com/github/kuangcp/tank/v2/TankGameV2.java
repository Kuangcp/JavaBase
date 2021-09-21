package com.github.kuangcp.tank.v2;

import javax.swing.*;

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
        MainPanelV2 panel = new MainPanelV2();
        this.add(panel);
        this.addKeyListener(panel);

        this.setLocation(900, 200);
        this.setSize(405, 333);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}