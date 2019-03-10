package com.github.kuangcp.port;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LogicThreadTest {

  public static void main(String[] args) {
    ServerSocket serverSocket;
    Socket socket;
    try {
      serverSocket = new ServerSocket(1000);
      socket = serverSocket.accept();
      LogicThread t = new LogicThread(socket);
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
