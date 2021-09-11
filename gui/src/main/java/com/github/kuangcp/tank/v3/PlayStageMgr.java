package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.util.TankTool;
import lombok.extern.slf4j.Slf4j;

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

    static int round = 0;
    public List<EnemyTank> enemyTanks;
    public List<Brick> bricks; // 砖
    public List<Iron> irons; // 铁

    public static boolean hasStart() {
        return Objects.nonNull(PlayStageMgr.instance) && PlayStageMgr.instance.startLogic;
    }

    public void markStopLogic() {
        this.startLogic = false;
    }

    public void markStartLogic() {
        this.startLogic = true;
        round++;
        log.info("start round {}", round);
    }

    public static void init(Hero hero, List<EnemyTank> ets, List<Brick> bricks, List<Iron> irons) {
        instance = new PlayStageMgr(hero, ets, bricks, irons);
    }

    private PlayStageMgr(Hero hero, List<EnemyTank> enemyTanks, List<Brick> bricks, List<Iron> irons) {
        this.hero = hero;
        this.enemyTanks = enemyTanks;
        this.bricks = bricks;
        this.irons = irons;
    }

//    public boolean ableToMove(Hero hero) {
//        return ets.stream().allMatch(v -> TankTool.ablePass(hero, v))
//                && bricks.stream().allMatch(v -> TankTool.ablePass(hero, v))
//                && irons.stream().allMatch(v -> TankTool.ablePass(hero, v));
//    }

    public boolean ableToMove(Hero hero) {
        return enemyTanks.stream().allMatch(v -> TankTool.ablePass(hero, v));
    }
}
