package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-13 01:13
 */
@Slf4j
public class VictoryImgMgr extends AbstractImgListMgr {

    public static VictoryImgMgr instance = new VictoryImgMgr();

    public VictoryImgMgr() {
        super.width = 760;
        super.height = 650;

        // super.imgPathArr = new String[]{"/images/Win2.jpg"};
    }

    public String getConfigKey(){
        return PropertiesMgr.Key.Img.ROUND_VICTORY;
    }
}
