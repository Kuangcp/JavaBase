package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.resource.ResourceMgr;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
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
        MonitorExecutor.init();
        try {
            ResourceMgr.loadResource();
            log.info("finish load resources");
            EventQueue.invokeLater(new MainFrame());
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
