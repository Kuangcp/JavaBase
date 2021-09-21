package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * @author https://github.com/kuangcp on 2021-09-21 10:05
 */
@Slf4j
public class PropertiesMgr {

    public static class Key {

        public static interface Img {
            String avatar = "round.avatar";
            String defeat = "round.defeat";
            String victory = "round.victory";
            String bomb = "animation.bomb";
        }
    }

    public static Properties imgProperties = null;

    public static void init(){
        imgProperties = new Properties();
        try {
            imgProperties.load(PropertiesMgr.class.getResourceAsStream("/tank/conf/img.properties"));
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
