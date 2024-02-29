package com.github.kuangcp.port;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class LogicThreadTest {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(10000);
            socket = serverSocket.accept();
            LogicThread t = new LogicThread(socket);
            t.start();
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
