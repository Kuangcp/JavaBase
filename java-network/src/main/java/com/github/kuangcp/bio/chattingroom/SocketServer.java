package com.github.kuangcp.bio.chattingroom;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/4/2 0002
 * 服务器端
 * 使用多线程来是实现简单的CS架构的公共聊天室
 */
@Slf4j
public class SocketServer {

  //定义保存所有的socket的集合，并包装成线程安全的
  static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());

  public static void main(String[] s) throws Exception {
    ServerSocket server = new ServerSocket(30000);
    while (true) {
      Socket socket = server.accept();
      socketList.add(socket);
      log.info("establish connect: socket={}", socket);

      //每当客户端连接后启动一个ServerThread 线程为该客户服务
      new Thread(new ServerThread(socket)).start();
    }
  }
}
