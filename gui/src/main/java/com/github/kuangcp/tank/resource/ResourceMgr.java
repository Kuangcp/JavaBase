package com.github.kuangcp.tank.resource;

import com.github.kuangcp.tank.mgr.BombMgr;
import com.github.kuangcp.tank.v3.RoundMapMgr;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:35
 */
@Slf4j
public class ResourceMgr {

    public static void loadResource() {
        PropertiesMgr.init();

        log.info("[init] start load resource");

        // image
        BombMgr.instance.loadImg();
        DefeatImgMgr.instance.loadImg();
        AvatarImgMgr.instance.loadImg();
        VictoryImgMgr.instance.loadImg();

        RoundMapMgr.init();
    }
}
