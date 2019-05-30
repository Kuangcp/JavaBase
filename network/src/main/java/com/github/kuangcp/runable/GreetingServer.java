package com.github.kuangcp.runable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端
 * 输入线程阻塞的原因，不能同时收发，怎么处理？多线程？
 * 服务器不应该发出，而是做中转站
 */
public class GreetingServer extends Thread {

  private ServerSocket serverSocket;
  private static Logger logger = LoggerFactory.getLogger(GreetingServer.class);

  public GreetingServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(10000);
  }

  public void run() {
    while (true) {
      try {
        logger.info("#####  Waiting for client on port " + serverSocket.getLocalPort() + "...");
        Socket server = serverSocket.accept();
        Scanner scanner = new Scanner(System.in);

        logger.info("Just connected to " + server.getRemoteSocketAddress());
        DataInputStream in = new DataInputStream(server.getInputStream());
        logger.info("接收到的：" + in.readUTF());
        // 对客户端发出的消息
        DataOutputStream out = new DataOutputStream(server.getOutputStream());

        while (true) {
          String temp = scanner.nextLine();
          logger.info("input:" + temp);
          out.writeUTF(temp);
          if ("90".equals(temp)) {
            break;
          }

        }
        out.writeUTF(
            "Thank you for connecting to " + server.getLocalSocketAddress() + "  Goodbye!");

        server.close();
      } catch (SocketTimeoutException s) {
        logger.info("Socket timed out!");
        break;
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  public static void main(String[] args) {
//      int port = Integer.parseInt(args[0]);
    int port = 10000;
    try {
      Thread t = new GreetingServer(port);
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
