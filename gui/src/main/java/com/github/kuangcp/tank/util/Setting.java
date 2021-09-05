package com.github.kuangcp.tank.util;


import com.github.kuangcp.tank.constant.SettingCommand;
import com.github.kuangcp.tank.v1.Hero;
import com.github.kuangcp.tank.v2.Shot;
import com.github.kuangcp.tank.v3.MyPanel3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Setting extends JFrame implements ActionListener {

    JPanel up, ss;
    JLabel U;
    JButton[] Plus = new JButton[4];
    JButton[] Cut = new JButton[4];
    JLabel[] Kind = new JLabel[4];
    Hero hero;

    public Setting(Hero hero) {
        this.hero = hero;
        try {
            final BufferedImage iconImg = ImageIO.read(getClass().getResourceAsStream("/images/Me4.jpg"));
            U = new JLabel(new ImageIcon(iconImg));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++) {
            Plus[i] = new JButton("+");
            Cut[i] = new JButton("-");
        }
        Kind[0] = new JLabel("子弹速度");//+Shot.getSpeed()
        Kind[1] = new JLabel("̹坦克速度");//+hero.getSpeed()
        Kind[2] = new JLabel("坦克生命");//+this.hero.getLife()
        Kind[3] = new JLabel("敌人数量");//+MyPanel3.getEnSize()

        up = new JPanel();
        ss = new JPanel();

        up.add(U);

        ss.setLayout(new GridLayout(4, 3, 0, 0));
        for (int i = 0; i < 4; i++) {
            ss.add(Kind[i]);
            ss.add(Plus[i]);
            ss.add(Cut[i]);
            Plus[i].addActionListener(this);
            Cut[i].addActionListener(this);
        }
        Plus[0].setActionCommand(SettingCommand.SHOT_SPEED_INCREMENT);
        Plus[1].setActionCommand(SettingCommand.TANK_SPEED_INCREMENT);
        Plus[2].setActionCommand(SettingCommand.TANK_HP_INCREMENT);
        Plus[3].setActionCommand(SettingCommand.DEMONS_COUNT_INCREMENT);

        Cut[0].setActionCommand(SettingCommand.SHOT_SPEED_DECREMENT);
        Cut[1].setActionCommand(SettingCommand.TANK_SPEED_DECREMENT);
        Cut[2].setActionCommand(SettingCommand.TANK_HP_DECREMENT);
        Cut[3].setActionCommand(SettingCommand.DEMONS_COUNT_DECREMENT);

        this.add(up, BorderLayout.NORTH);
        this.add(ss, BorderLayout.CENTER);
        this.setLocation(800, 300);
        this.setSize(260, 280);
        this.setTitle("Setting");
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(SettingCommand.SHOT_SPEED_INCREMENT)) {
            Shot.setSpeed(Shot.getSpeed() + 1);
        }
        if (event.getActionCommand().equals(SettingCommand.SHOT_SPEED_DECREMENT)) {
            Shot.setSpeed(Shot.getSpeed() - 1);
        }

        if (event.getActionCommand().equals(SettingCommand.TANK_SPEED_INCREMENT)) {
            hero.addSpeed(1);
        }
        if (event.getActionCommand().equals(SettingCommand.TANK_SPEED_DECREMENT)) {
            hero.addSpeed(-1);
        }

        if (event.getActionCommand().equals(SettingCommand.TANK_HP_INCREMENT)) {
            this.hero.setLife(this.hero.getLife() + 1);
        }
        if (event.getActionCommand().equals(SettingCommand.TANK_HP_DECREMENT)) {
            this.hero.setLife(this.hero.getLife() - 1);
        }

        if (event.getActionCommand().equals(SettingCommand.DEMONS_COUNT_INCREMENT)) {
            MyPanel3.setEnSize(MyPanel3.getEnSize() + 1);
        }
        if (event.getActionCommand().equals(SettingCommand.DEMONS_COUNT_DECREMENT)) {
            MyPanel3.setEnSize(MyPanel3.getEnSize() - 1);
        }

    }
}