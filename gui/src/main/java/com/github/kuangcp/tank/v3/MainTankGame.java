package com.github.kuangcp.tank.v3;

public class MainTankGame {

    /**
     * -Xmx300m -Xms300m -XX:+UseG1GC
     */
    public static void main(String[] args) {
        Thread t = new Thread(new TankFrame());
        t.setName("mainFrame");
        t.start();
    }
}
