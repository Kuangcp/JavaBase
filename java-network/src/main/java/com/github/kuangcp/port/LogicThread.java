package com.github.kuangcp.port;

import com.github.kuangcp.util.ResourcesUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 服务器端逻辑线程
 */
public class LogicThread extends Thread {

  private Socket socket;
  private InputStream is;
  private OutputStream os;

  public LogicThread(Socket socket) {
    this.socket = socket;
    start(); //启动线程
  }

  public void run() {
    byte[] b = new byte[1024];
    try {
      //初始化流
      os = socket.getOutputStream();
      is = socket.getInputStream();
      for (int i = 0; i < 3; i++) {
        //读取数据
        int n = is.read(b);
        //逻辑处理
        byte[] response = logic(b, 0, n);
        //反馈数据
        os.write(response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        ResourcesUtil.close(os, is, socket);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 逻辑处理方法,实现echo逻辑
   *
   * @param b 客户端发送数据缓冲区
   * @param off 起始下标
   * @param len 有效数据长度
   */
  private byte[] logic(byte[] b, int off, int len) {
    byte[] response = new byte[len];
    //将有效数据拷贝到数组response中
    System.arraycopy(b, 0, response, 0, len);
    return response;
  }
}