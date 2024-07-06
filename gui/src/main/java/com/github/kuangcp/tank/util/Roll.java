package com.github.kuangcp.tank.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-06 22:39
 */
public class Roll {

    /**
     * @param perMil 千分率 ‰
     */
    public static boolean roll(int perMil) {
        return ThreadLocalRandom.current().nextInt(1000) <= perMil;
    }
}
