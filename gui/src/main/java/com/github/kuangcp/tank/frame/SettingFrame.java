package com.github.kuangcp.tank.frame;

import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.constant.ButtonCommand;
import com.github.kuangcp.tank.constant.SettingCommand;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.Hero;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class SettingFrame extends JFrame implements ActionListener {

    public static SettingFrame instance = null;

    JPanel buttonArea;
    JButton[] incrArr = new JButton[4];
    JButton[] decrArr = new JButton[4];
    JLabel[] labelArr = new JLabel[4];
    JLabel[] valArr = new JLabel[]{new JLabel(), new JLabel(), new JLabel(), new JLabel()};

    public static synchronized void activeFocus() {
        final SettingFrame frame = getInstance();
        frame.setVisible(true);
    }

    private static synchronized SettingFrame getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SettingFrame();
        }
        return instance;
    }

    public SettingFrame() {
        for (int i = 0; i < 4; i++) {
            incrArr[i] = new JButton("+");
            decrArr[i] = new JButton("-");
        }
        labelArr[0] = new JLabel("子弹速度");//+Shot.getSpeed()
        labelArr[3] = new JLabel("敌人数量");//+MyPanel3.getEnSize()
        labelArr[1] = new JLabel("玩家速度");//+hero.getSpeed()
        labelArr[2] = new JLabel("玩家生命");//+this.hero.getLife()

        buttonArea = new JPanel();

        buttonArea.setLayout(new GridLayout(4, 4, 0, 0));
        // 四行
        for (int i = 0; i < 4; i++) {
            // 四列
            buttonArea.add(labelArr[i]);
            buttonArea.add(valArr[i]);
            buttonArea.add(incrArr[i]);
            buttonArea.add(decrArr[i]);

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

        this.refreshValLabel();

        this.add(buttonArea, BorderLayout.CENTER);
        this.setLocation(800, 300);
        this.setSize(260, 280);
        this.setTitle("Setting");
        this.setVisible(true);
    }

    public void refreshValLabel() {
        final Optional<Hero> heroOpt = Optional.ofNullable(PlayStageMgr.instance).map(v -> v.hero);
        this.valArr[0].setText(Bullet.getSpeed() + "");
        this.valArr[1].setText(heroOpt.map(v -> v.getSpeed() + "").orElse(""));
        this.valArr[2].setText(heroOpt.map(v -> v.getLife() + "").orElse(""));
        this.valArr[3].setText(PlayStageMgr.enemySize + "");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (PlayStageMgr.stageNoneStart()) {
            return;
        }

        if (event.getActionCommand().equals(ButtonCommand.SETTING_FRAME)) {
            SettingFrame.activeFocus();
        }

        if (event.getActionCommand().equals(SettingCommand.SHOT_SPEED_INCREMENT)) {
            Bullet.setSpeed(Bullet.getSpeed() + 1);
        }
        if (event.getActionCommand().equals(SettingCommand.SHOT_SPEED_DECREMENT)) {
            Bullet.setSpeed(Bullet.getSpeed() - 1);
        }

        if (event.getActionCommand().equals(SettingCommand.TANK_SPEED_INCREMENT)) {
            PlayStageMgr.instance.hero.addSpeed(1);
        }
        if (event.getActionCommand().equals(SettingCommand.TANK_SPEED_DECREMENT)) {
            PlayStageMgr.instance.hero.addSpeed(-1);
        }

        if (event.getActionCommand().equals(SettingCommand.TANK_HP_INCREMENT)) {
            PlayStageMgr.instance.hero.addLife(1);
        }
        if (event.getActionCommand().equals(SettingCommand.TANK_HP_DECREMENT)) {
            PlayStageMgr.instance.hero.addLife(-1);
        }

        if (event.getActionCommand().equals(SettingCommand.DEMONS_COUNT_INCREMENT)) {
            PlayStageMgr.addEnemySize(1);
        }
        if (event.getActionCommand().equals(SettingCommand.DEMONS_COUNT_DECREMENT)) {
            PlayStageMgr.addEnemySize(-1);
        }

        this.refreshValLabel();
    }
}