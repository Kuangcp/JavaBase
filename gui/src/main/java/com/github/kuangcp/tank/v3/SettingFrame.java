package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.constant.SettingCommand;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.Hero;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class SettingFrame extends JFrame implements ActionListener {

    public static SettingFrame instance = null;

    JPanel up, ss;
    JLabel U;
    JButton[] incrArr = new JButton[4];
    JButton[] decrArr = new JButton[4];
    JLabel[] labelArr = new JLabel[4];
    Hero hero;

    public static synchronized void activeFocus(Hero hero) {
        final SettingFrame frame = getInstance(hero);
        frame.setVisible(true);
    }

    private static synchronized SettingFrame getInstance(Hero hero) {
        if (Objects.isNull(instance)) {
            instance = new SettingFrame(hero);
        }
        instance.hero = hero;
        return instance;
    }

    public SettingFrame(Hero hero) {
        this.hero = hero;
        try {
            final BufferedImage iconImg = ImageIO.read(getClass().getResourceAsStream("/images/Me4.jpg"));
            U = new JLabel(new ImageIcon(iconImg));
        } catch (IOException e) {
            log.error("", e);
        }

        for (int i = 0; i < 4; i++) {
            incrArr[i] = new JButton("+");
            decrArr[i] = new JButton("-");
        }
        labelArr[0] = new JLabel("子弹速度");//+Shot.getSpeed()
        labelArr[1] = new JLabel("̹坦克速度");//+hero.getSpeed()
        labelArr[2] = new JLabel("坦克生命");//+this.hero.getLife()
        labelArr[3] = new JLabel("敌人数量");//+MyPanel3.getEnSize()

        up = new JPanel();
        ss = new JPanel();

        up.add(U);

        ss.setLayout(new GridLayout(4, 3, 0, 0));
        for (int i = 0; i < 4; i++) {
            ss.add(labelArr[i]);
            ss.add(incrArr[i]);
            ss.add(decrArr[i]);
            incrArr[i].addActionListener(this);
            decrArr[i].addActionListener(this);
        }

        incrArr[0].setActionCommand(SettingCommand.SHOT_SPEED_INCREMENT);
        incrArr[1].setActionCommand(SettingCommand.TANK_SPEED_INCREMENT);
        incrArr[2].setActionCommand(SettingCommand.TANK_HP_INCREMENT);
        incrArr[3].setActionCommand(SettingCommand.DEMONS_COUNT_INCREMENT);

        decrArr[0].setActionCommand(SettingCommand.SHOT_SPEED_DECREMENT);
        decrArr[1].setActionCommand(SettingCommand.TANK_SPEED_DECREMENT);
        decrArr[2].setActionCommand(SettingCommand.TANK_HP_DECREMENT);
        decrArr[3].setActionCommand(SettingCommand.DEMONS_COUNT_DECREMENT);

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
            Bullet.setSpeed(Bullet.getSpeed() + 1);
        }
        if (event.getActionCommand().equals(SettingCommand.SHOT_SPEED_DECREMENT)) {
            Bullet.setSpeed(Bullet.getSpeed() - 1);
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
            PlayStageMgr.addEnemySize(1);
            log.info("{}",  PlayStageMgr.getEnemySize());
        }
        if (event.getActionCommand().equals(SettingCommand.DEMONS_COUNT_DECREMENT)) {
            PlayStageMgr.addEnemySize(-1);
        }

    }
}