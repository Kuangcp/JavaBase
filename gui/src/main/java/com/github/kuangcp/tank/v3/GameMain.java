package com.github.kuangcp.tank.v3;

public class GameMain {

    public static void main(String[] args) {
        TankFrame T = new TankFrame();
        Thread t = new Thread(T);
        t.start();
    }
}
