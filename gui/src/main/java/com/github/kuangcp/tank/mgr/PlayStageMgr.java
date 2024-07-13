package com.github.kuangcp.tank.mgr;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Bullet;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Hinder;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.Tank;
import com.github.kuangcp.tank.resource.DefeatImgMgr;
import com.github.kuangcp.tank.resource.VictoryImgMgr;
import com.github.kuangcp.tank.util.ExecutePool;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.domain.event.DelayEvent;
import com.github.kuangcp.tank.util.executor.DelayExecutor;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-11 23:24
 */
@Slf4j
public class PlayStageMgr {

    public static PlayStageMgr instance = null;

    public static Hero hero;
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
    @Getter
    public static int enemySize = 0;
    /**
     * 无敌状态 时间
     */
    @Getter
    static long invincibleMs = 5_000L;

    public static volatile boolean newStage = true;
    public static volatile boolean invokeNewStage = false;

    // 场景 上下文
//    public List<EnemyTank> enemyTanks;
//    public List<Brick> bricks; // 砖
//    public List<Iron> irons; // 铁

    public Map<Integer, Hero> heroMap = new ConcurrentHashMap<>();

    public static List<EnemyTank> enemyList;
    // todo 转移
    public static List<Brick> bricks;
    public static List<Iron> irons;

    //所有按下键的code集合
    public static int[][] enemyTankMap = new int[12][2];
    public static int[] myself = new int[6];

    /**
     * 等待stage启动
     */
    public static boolean stageNoneStart() {
        return Objects.isNull(PlayStageMgr.instance) || !PlayStageMgr.instance.startLogic;
    }

    public void markStopLogic() {
        this.startLogic = false;
    }

    public void markStartLogic() {
        this.startLogic = true;
        round++;
        log.warn("start round:{}", round);
    }

    /**
     * start new stage
     */
    public static void init(Hero hero, List<EnemyTank> enemyTanks, List<Brick> bricks, List<Iron> irons) {
        instance = new PlayStageMgr(hero, enemyTanks, bricks, irons);
    }

    private PlayStageMgr(Hero hero, List<EnemyTank> enemyTanks, List<Brick> bricks, List<Iron> irons) {
        this.hero = hero;
        this.enemyList = enemyTanks;
        this.bricks = bricks;
        this.irons = irons;
    }

    public boolean hasWinCurrentRound() {
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


    public void refresh() {
        bricks.removeIf(Hinder::isDead);
        irons.removeIf(Iron::isDead);

        // hero bullet
        for (int i = 0; i < PlayStageMgr.hero.bulletList.size(); i++) {
            Bullet myBullet = PlayStageMgr.hero.bulletList.get(i);
            for (Brick brick : bricks) {
                TankTool.judgeHint(myBullet, brick);
            }
            for (Iron iron : irons) {
                TankTool.judgeHint(myBullet, iron);
            }
            if (myBullet.x < 440 && myBullet.x > 380 && myBullet.y < 540 && myBullet.y > 480) {
                myBullet.alive = false;
                PlayStageMgr.hero.setAlive(false);
            }
        }
        PlayStageMgr.hero.bulletList.removeIf(Bullet::isDead);

        /*敌人子弹*/
        // FIXME ConcurrentModificationException
        for (EnemyTank et : enemyList) {
            for (int i = 0; i < et.bulletList.size(); i++) {
                Bullet myBullet = et.bulletList.get(i);
                for (Brick brick : bricks) {
                    TankTool.judgeHint(myBullet, brick);
                }
                for (Iron iron : irons) {
                    TankTool.judgeHint(myBullet, iron);
                }
                if (myBullet.x < 440 && myBullet.x > 380 && myBullet.y < 540 && myBullet.y > 480) {
                    myBullet.alive = false;
                    PlayStageMgr.hero.setAlive(false);
                }
            }
            et.bulletList.removeIf(Bullet::isDead);
        }

        //坦克少于5个就自动添加4个
        if (enemyList.size() < 5) {
            for (int i = 0; i < 4; i++) {
                EnemyTank d = new EnemyTank(20 + (int) (Math.random() * 400), 20 + (int) (Math.random() * 300), i % 4);
                LoopEventExecutor.addLoopEvent(d);
                enemyList.add(d);
            }
        }

        for (int i = 0; i < enemyList.size(); i++) {
            EnemyTank demon = enemyList.get(i);
            if (demon.isAlive()) {
                BombMgr.instance.checkBong(demon, PlayStageMgr.hero.bulletList);
            } else {
                // TODO 去掉 延迟删除逻辑 子弹统一管理 而不是tank子属性
//                enemyList.remove(demon);

                // 延迟删除 敌人和子弹
                if (demon.delayRemove) {
                    continue;
                }
                demon.delayRemove = true;

                DelayExecutor.addEvent(new DelayEvent(7_000) {
                    @Override
                    public void run() {
                        enemyList.remove(demon);
                    }
                });
            }
        }

        if (PlayStageMgr.instance.hasWinCurrentRound() || !PlayStageMgr.hero.isAlive()) {
            PlayStageMgr.instance.stopStage();
        }
    }

    public static void startNewRound() {
        enemyList = new CopyOnWriteArrayList<>();
        bricks = new CopyOnWriteArrayList<>();
        irons = new CopyOnWriteArrayList<>();

        //创建英雄坦克
        if (newStage) {
            hero = new Hero(480, 500, 3);
            hero.setLife(10);
        } else {
            hero = new Hero(myself[0], myself[1], 3);
            hero.setLife(myself[2]);
            hero.setPrize(myself[3]);
        }

        PlayStageMgr.init(hero, enemyList, bricks, irons);

        //多键监听实现
//        heroKeyListener = new HeroKeyListener(HoldingKeyEventMgr.instance, hero, this);
//        ExecutePool.exclusiveLoopPool.execute(heroKeyListener);
        ExecutePool.exclusiveLoopPool.execute(hero);

        // 创建 敌人的坦克
        EnemyTank ett = null;
        if (newStage) {//正常启动并创建坦克线程
            for (int i = 0; i < PlayStageMgr.getEnemySize(); i++) {
                //在四个随机区域产生坦克
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 1:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 20 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 2:
                        ett = new EnemyTank(20 + (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), i % 4);
                        break;
                    case 3:
                        ett = new EnemyTank(700 - (int) (Math.random() * 30), 200 + (int) (Math.random() * 30), i % 4);
                        break;
                }

                if (Objects.isNull(ett)) {
                    continue;
                }
                LoopEventExecutor.addLoopEvent(ett);
                enemyList.add(ett);
            }
        } else {
            /*进入读取文件步骤*/
            for (int i = 0; i < enemyTankMap.length; i++) {
                if (enemyTankMap[i][0] == 0) {
                    break;
                }
                ett = new EnemyTank(enemyTankMap[i][0], enemyTankMap[i][1], i % 4);
                LoopEventExecutor.addLoopEvent(ett);
                enemyList.add(ett);
            }
        }

        //左右下角
        createB(bricks, 20, 310, 300, 540);
        createB(bricks, 520, 310, 740, 540);

        //头像附近
        createB(bricks, 360, 460, 460, 480);
        createB(bricks, 360, 480, 380, 540);
        createB(bricks, 440, 460, 460, 540);

        createI(irons, 330, 410, 480, 430);

        PlayStageMgr.instance.markStartLogic();
    }

    /**
     * 创建砖
     */
    public static void createB(List<Brick> bricks, int startX, int startY, int endX, int endY) {
        Brick template = new Brick(0, 0);
        for (int i = startX; i < endX; i += template.getWidth()) {
            for (int j = startY; j < endY; j += template.getHeight()) {
                Brick bs = new Brick(i, j);
                bricks.add(bs);
            }
        }
    }

    /**
     * 创建铁块
     */
    public static void createI(List<Iron> irons, int startX, int startY, int endX, int endY) {
        Iron template = new Iron(0, 0);
        for (int i = startX; i < endX; i += template.getWidth()) {
            for (int j = startY; j < endY; j += template.getHeight()) {
                Iron bs = new Iron(i, j);
                irons.add(bs);
            }
        }
    }


    public void stopStage() {
        roundPrize = hero.getPrize();
        instance.markStopLogic();
        log.info("clean round:{}", round);
        instance.hero.setAlive(false);
        // TODO clean
        for (EnemyTank et : enemyList) {
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
        return enemyList.stream().allMatch(v -> TankTool.ablePass(hero, v));
    }

    public boolean willInBorder(Tank tank) {
        if (Objects.isNull(tank)) {
            return false;
        }
        switch (tank.direct) {
            case DirectType.UP:
                return tank.y - tank.getHalfHeight() - tank.getSpeed() > RoundMapMgr.instance.border.getMinY();
            case DirectType.DOWN:
                return tank.y + tank.getHalfHeight() + tank.getSpeed() < RoundMapMgr.instance.border.getMaxY();
            case DirectType.LEFT:
                return tank.x - tank.getHalfHeight() - tank.getSpeed() > RoundMapMgr.instance.border.getMinX();
            case DirectType.RIGHT:
                return tank.x + tank.getHalfHeight() + tank.getSpeed() < RoundMapMgr.instance.border.getMaxX();
        }
        return false;
    }

    public boolean willInBorder(Bullet bullet) {
        if (Objects.isNull(bullet)) {
            return false;
        }
        return bullet.x <= RoundMapMgr.instance.border.getMinX() || bullet.x >= RoundMapMgr.instance.border.getMaxX()
                || bullet.y <= RoundMapMgr.instance.border.getMinY() || bullet.y >= RoundMapMgr.instance.border.getMaxY();
    }

    public static void setEnemySize(int enemySize) {
        PlayStageMgr.enemySize = enemySize;
    }

    public static void addEnemySize(int delta) {
        PlayStageMgr.enemySize += delta;
    }

    public static boolean ablePassByHero(Tank t) {
        if (Objects.isNull(instance) || Objects.isNull(instance.hero) || !instance.hero.isAlive()) {
            return true;
        }

        return TankTool.ablePass(t, instance.hero);
    }

    public static boolean ablePassByHinder(Tank t) {
        if (Objects.isNull(instance) || Objects.isNull(instance.hero) || !instance.hero.isAlive()) {
            return true;
        }

        for (int i = 0; i < instance.bricks.size(); i++) {
            final boolean pass = TankTool.ablePass(t, instance.bricks.get(i));
            if (!pass) {
                return false;
            }
        }

        for (int i = 0; i < instance.irons.size(); i++) {
            final boolean pass = TankTool.ablePass(t, instance.irons.get(i));
            if (!pass) {
                return false;
            }
        }

        return true;
    }

    public int getLiveEnemy() {
        int result = 0;
        for (final EnemyTank enemyTank : this.enemyList) {
            if (enemyTank.isAlive()) {
                result++;
            }
        }
        return result;
    }
}
