package com.github.kuangcp.tank.v3;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class MainTankGame {

    /**
     * -Xmx300m -Xms300m -XX:+UseG1GC
     * <p>
     * https://stackoverflow.com/questions/6736906/why-does-java-swings-setvisible-take-so-long
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new MainFrame());
    }
}
