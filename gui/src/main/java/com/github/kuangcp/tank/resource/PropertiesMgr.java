package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-21 10:05
 */
@Slf4j
public class PropertiesMgr {

    public interface Key {

        interface Img {
            String ROUND_AVATAR = "round.avatar";
            String ROUND_DEFEAT = "round.defeat";
            String ROUND_VICTORY = "round.victory";

            String ANIMATION_BOMB = "animation.bomb";
            String START_BG = "round.start";
        }
    }

    public static Properties imgProperties = null;

    public static void init() {
        imgProperties = new Properties();
        try {
            imgProperties.load(PropertiesMgr.class.getResourceAsStream("/tank/conf/img.properties"));
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
