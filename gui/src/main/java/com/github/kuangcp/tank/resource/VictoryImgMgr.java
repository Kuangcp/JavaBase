package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:13
 */
@Slf4j
public class VictoryImgMgr extends AbstractImgListMgr {

    public static VictoryImgMgr instance = new VictoryImgMgr();

    public VictoryImgMgr() {
        super.width = 760;
        super.height = 650;

        super.imgPathArr = new String[]{"/images/Win2.jpg"};
    }
}
