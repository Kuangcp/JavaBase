package com.github.kuangcp.tank.panel;


import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.panel.event.HeroInfoPanelRefreshEvent;
import com.github.kuangcp.tank.v3.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * 就是一个用来显示属性的画板
 * JLabel 被add后 就算他发生改变，对之前已经add了的组件 不会有影响
 * 但是可以用setText来进行改变
 */
@Slf4j
@SuppressWarnings("serial")
public class HeroInfoPanel extends JPanel {

    JLabel tankCounter;
    JLabel prizeNo;
    List<EnemyTank> ets;
    HeroInfoPanelRefreshEvent refreshEvent;

    static final Tank heroIcon = new Tank(15, 20, 0) {
        @Override
        public void drawSelf(Graphics g) {
            g.setColor(Color.yellow);
            super.drawSelf(g);
        }

        @Override
        public void run() {

        }
    };
    static final Tank enemyIcon = new Tank(70, 20, 0) {
        @Override
        public void drawSelf(Graphics g) {
            g.setColor(Color.cyan);
            super.drawSelf(g);
        }

        @Override
        public void run() {

        }
    };

    public void exit() {
        if (Objects.nonNull(refreshEvent)) {
            this.refreshEvent.stop();
        }
    }

    public void setEts(List<EnemyTank> ets) {
        this.ets = ets;
    }

    public HeroInfoPanel(JLabel tankCounter, List<EnemyTank> ets, JLabel prizeNo) {
        this.tankCounter = tankCounter;
        this.ets = ets;
        this.prizeNo = prizeNo;

        tankCounter.setLocation(15, -20);
    }

    public void paint(Graphics g) {
        super.paint(g);

        heroIcon.drawSelf(g);
        enemyIcon.drawSelf(g);
    }

    public void refreshData() {
        if (PlayStageMgr.stageNoneStart() || PlayStageMgr.pause) {
            return;
        }

        final String format = "      : %d        : %d";
        final String txt;
        if (PlayStageMgr.instance.hero.isAlive()) {
            txt = String.format(format, PlayStageMgr.instance.hero.getLife(), ets.size());
        } else {
            txt = String.format(format, 0, ets.size());
        }
        tankCounter.setText(txt);

        prizeNo.setText("战绩: " + PlayStageMgr.instance.hero.getPrize());
    }

    public void setRefreshEvent(HeroInfoPanelRefreshEvent refreshEvent) {
        this.refreshEvent = refreshEvent;
    }
}
