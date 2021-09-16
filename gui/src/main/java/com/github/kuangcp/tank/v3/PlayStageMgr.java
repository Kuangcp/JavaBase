package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.resource.DefeatImgMgr;
import com.github.kuangcp.tank.resource.VictoryImgMgr;
import com.github.kuangcp.tank.util.TankTool;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.Objects;

/**
 * @author https://github.com/kuangcp on 2021-09-11 23:24
 */
@Slf4j
public class PlayStageMgr {

    public static PlayStageMgr instance = null;

    public Hero hero;
    public boolean startLogic = false;
    public boolean winCurRound = false;
    public int roundPrize = 0;

    static int round = 0;
    /**
     * 全局游戏暂停
     */
    public static volatile boolean pause = false;

    // 游戏参数 配置
    /**
     * 敌人的数量
     */
    static int enemySize = 36;
    /**
     * 无敌状态 时间
     */
    static long invincibleMs = 3000L;

    // 场景 上下文
    public List<EnemyTank> enemyTanks;
    public List<Brick> bricks; // 砖
    public List<Iron> irons; // 铁

    /**
     * 等待stage启动
     */
    public static boolean waitStart() {
        return Objects.isNull(PlayStageMgr.instance) || !PlayStageMgr.instance.startLogic;
    }

    public void markStopLogic() {
        this.startLogic = false;
    }

    public void markStartLogic() {
        this.startLogic = true;
        round++;
        log.info("start round:{}", round);
    }

    public static void init(Hero hero, List<EnemyTank> enemyTanks, List<Brick> bricks, List<Iron> irons) {
        instance = new PlayStageMgr(hero, enemyTanks, bricks, irons);
    }

    private PlayStageMgr(Hero hero, List<EnemyTank> enemyTanks, List<Brick> bricks, List<Iron> irons) {
        this.hero = hero;
        this.enemyTanks = enemyTanks;
        this.bricks = bricks;
        this.irons = irons;
    }

    public boolean hasWinCurrentRound() {
        final Hero hero = instance.hero;
        if (Objects.isNull(hero)) {
            winCurRound = false;
        } else {
            winCurRound = hero.getPrize() >= 30;
        }
        return winCurRound;
    }

    public void abortStage() {
        this.winCurRound = false;
        this.stopStage();
    }

    public void stopStage() {
        roundPrize = hero.getPrize();
        instance.markStopLogic();
        log.info("clean round:{}", round);
        instance.hero.setAlive(false);
        // TODO clean
        for (EnemyTank et : enemyTanks) {
            et.setAbort(true);
            et.setAlive(false);
        }
    }

    public static void drawStopIgnore(Graphics g, ImageObserver observer) {
        if (Objects.isNull(instance)) {
            return;
        }
        instance.drawStop(g, observer);
    }

    public void drawStop(Graphics g, ImageObserver observer) {
        g.setColor(Color.YELLOW);
        if (winCurRound) {
            g.drawImage(VictoryImgMgr.instance.curImg, 0, 0,
                    VictoryImgMgr.instance.width, VictoryImgMgr.instance.height, observer);
        } else {
            g.drawImage(DefeatImgMgr.instance.curImg, 0, 0,
                    DefeatImgMgr.instance.width, DefeatImgMgr.instance.height, observer);
        }
        g.drawString("您的总成绩为：" + roundPrize, 320, 500);
    }

    // FIXME
//    public boolean ableToMove(Hero hero) {
//        return ets.stream().allMatch(v -> TankTool.ablePass(hero, v))
//                && bricks.stream().allMatch(v -> TankTool.ablePass(hero, v))
//                && irons.stream().allMatch(v -> TankTool.ablePass(hero, v));
//    }

    public boolean ableToMove(Hero hero) {
        return enemyTanks.stream().allMatch(v -> TankTool.ablePass(hero, v));
    }

    public static int getEnemySize() {
        return enemySize;
    }

    public static void setEnemySize(int enemySize) {
        PlayStageMgr.enemySize = enemySize;
    }

    public static void addEnemySize(int delta) {
        PlayStageMgr.enemySize += delta;
    }

    public static long getInvincibleMs() {
        return invincibleMs;
    }
}
