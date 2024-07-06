package com.github.kuangcp.tank.mgr;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.Iron;
import com.github.kuangcp.tank.domain.StageBorder;

import java.util.List;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-25 15:57
 */
public class RoundMapMgr {

    public static final RoundMapMgr instance = new RoundMapMgr();
    public List<Brick> bricks; // 砖
    public List<Iron> irons; // 铁

    public StageBorder border = null;

    public static void init() {
        instance.border = new StageBorder(20, 740, 20, 540);
//        instance.border = new StageBorder(20, 1600, 20, 900);
    }
}
