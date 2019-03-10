package com.github.kuangcp.bio.onechatone;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Myth on 2017/4/3 0003
 */
public class Server {

  static ChatMaps<String, PrintStream> clients = new ChatMaps<>();

  private void init() {
    try {
      ServerSocket ss = new ServerSocket(ChatProtocol.SERVER_PORT);
      while (true) {
        Socket socket = ss.accept();
        new ServerThread(socket).start();
      }
    } catch (Exception r) {
      System.out.println("服务启动失败，是否端口" + ChatProtocol.SERVER_PORT + "被占用");
      r.printStackTrace();
    }
  }

  public static void main(String[] s) {
    Server server = new Server();
    server.init();
  }
}
