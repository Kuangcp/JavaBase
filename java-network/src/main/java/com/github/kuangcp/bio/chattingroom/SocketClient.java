package com.github.kuangcp.bio.chattingroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/4/2
 * 单线程的客户端
 */
@Slf4j
public class SocketClient {

  private String name;


  public static void main(String[] args) throws Exception {
    SocketClient client = new SocketClient();
    client.setName();
    client.start();
  }

  private void start() throws Exception {
    Socket socket = new Socket("127.0.0.1", 30000);
    //客户端启动线程不断的读取来自服务器的数据
    new Thread(new ClientThread(this.name, socket)).start();

    PrintStream printStream = new PrintStream(socket.getOutputStream());
    String line;
    //不断读取键盘输入
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while ((line = br.readLine()) != null) {
      //将键盘输入写入 Socket 输入流中
      printStream.println(this.name + " : " + line);
    }
  }

  private void setName() throws Exception {
    log.info("input name");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    name = br.readLine();
    log.info("your name: {}", name);
  }
}
