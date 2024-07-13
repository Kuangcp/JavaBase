package com.github.kuangcp.tank.resource;

import com.github.kuangcp.tank.mgr.BombMgr;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-13 01:35
 */
@Slf4j
public class ResourceMgr {

    public static void loadResource() throws IOException {
        PropertiesMgr.init();

        log.info("[init] start load resource");

        BombMgr.instance.loadImg();
        DefeatImgMgr.instance.loadImg();
        AvatarImgMgr.instance.loadImg();
        VictoryImgMgr.instance.loadImg();
    }
}
