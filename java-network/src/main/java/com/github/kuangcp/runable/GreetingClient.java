package com.github.kuangcp.runable;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端
 */
public class GreetingClient {

  private static Logger logger = LoggerFactory.getLogger(GreetingClient.class);

  public static void main(String[] s) {
//      In();
    String[] args = {"localhost", "10000"};
    String serverName = args[0];
    int port = Integer.parseInt(args[1]);

    try {
      Socket client = new Socket(serverName, port);
      while (true) {
        logger.info("### Connecting to " + serverName + " on port " + port);

        logger.info("Just connected to " + client.getRemoteSocketAddress());
        OutputStream outToServer = client.getOutputStream();

        DataOutputStream out = new DataOutputStream(outToServer);
        out.writeUTF("[客户端发送 ： Hello from " + client.getLocalSocketAddress() + "]");

        InputStream inFromServer = client.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
        logger.info("接收到服务器：" + in.readUTF());

        while (true) {
          InputStream inFromServer2 = client.getInputStream();
          DataInputStream in2 = new DataInputStream(inFromServer2);
          logger.info("接收到服务器：" + in2.readUTF());

          Scanner scanner = new Scanner(System.in);
          String temp = scanner.nextLine();
          out = new DataOutputStream(outToServer);
          out.writeUTF(temp);
          if ("9090".equals(temp)) {
            break;
          }
        }
        break;
      }
      client.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void In() {
//      Scanner scanner = new Scanner(System.in);
//      logger.info(scanner.nextLine());
//      scanner.nextLine();

    BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
    try {
      logger.info(sin.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
