package com.github.kuangcp.tank;

import com.github.kuangcp.tank.frame.MainFrame;
import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.resource.ResourceMgr;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
import com.github.kuangcp.tank.ws.WsBizHandler;
import com.github.kuangcp.websocket.WsServer;
import com.github.kuangcp.websocket.WsServerConfig;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class TankGame {

    /**
     * -Xmx300m -Xms300m -XX:+UseG1GC
     * <p>
     * https://stackoverflow.com/questions/6736906/why-does-java-swings-setvisible-take-so-long
     */
    public static void main(String[] args) {
        MonitorExecutor.init();
        try {
            ResourceMgr.loadResource();
            RoundMapMgr.init();
            log.info("finish load resources");

            startWsServer();

            EventQueue.invokeLater(new MainFrame());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void startWsServer() {
        final WsServerConfig config = new WsServerConfig();
        final WsServer wsServer = new WsServer(config, new WsBizHandler(config));
        final Thread wsThread = new Thread(wsServer::start);
        wsThread.setDaemon(true);
        wsThread.setName("ws-server");
        wsThread.start();
    }
}
