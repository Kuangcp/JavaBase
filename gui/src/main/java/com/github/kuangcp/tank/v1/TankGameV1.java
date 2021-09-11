package com.github.kuangcp.tank.v1;

import javax.swing.*;

/**
 * 1 绘画出坦克 并且能做到对它的移动操作
 * 2 移动最好封装成函数（面向对象思想）这是坦克的行为属性 可扩展
 * 3 加上对坦克运动的边界限定
 */
@SuppressWarnings("serial")
public class TankGameV1 extends JFrame {

    MyPanel mp;

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        TankGameV1 Tank = new TankGameV1();
    }

    //最外层JFrame的构造函数
    public TankGameV1() {
        mp = new MyPanel();

        this.add(mp);
        //注册键盘监听
        //下面的语句翻译为 ：当前类的监；听者是mp
        this.addKeyListener(mp);

        this.setLocation(900, 200);
        this.setSize(500, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}