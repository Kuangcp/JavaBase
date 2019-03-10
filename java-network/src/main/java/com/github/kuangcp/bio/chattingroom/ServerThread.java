package com.github.kuangcp.bio.chattingroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/4/2 0002
 * 负责处理每个线程通信的线程类
 */
@Slf4j
public class ServerThread implements Runnable {

  //定义当前线程处理的Socket
  private Socket s;
  //该线程处理的Socket对应的输入流
  private BufferedReader br;

  ServerThread(Socket s) throws IOException {
    this.s = s;
    //初始化该输入流
    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
  }

  @Override
  public void run() {
    try {
      String content;
      //不断的从Socket中读取客户端发送过来的数据
      while ((content = readFromClient()) != null) {
        log.info("#Chat# {}", content);
        //遍历socketList中的每个Socket
        for (Socket socket : SocketServer.socketList) {
          //将读到的内容向所有  Socket 发送
          PrintStream printStream = new PrintStream(socket.getOutputStream());
          printStream.println(content);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  //定义读取客户端数据的方法
  private String readFromClient() {
    try {
      return br.readLine();
      //如果捕获到异常，则表明该Socket对应的客户端已经关闭
    } catch (Exception e) {
      log.info("remove socket: socket={}", s);
      SocketServer.socketList.remove(s);
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
