package com.github.kuangcp.bio.onechatone;

import com.github.kuangcp.io.ResourceTool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

/**
 * Created by Myth on 2017/4/3 0003
 */
public class Client {

  private Socket socket;
  private PrintStream ps;
  private BufferedReader brServer;
  private BufferedReader keyIn;

  public static void main(String[] args) {
    Client client = new Client();
    client.init();
    client.readAndSend();
  }

  public void init() {
    try {
      keyIn = new BufferedReader(new InputStreamReader(System.in));
      socket = new Socket("127.0.0.1", ChatProtocol.SERVER_PORT);
      ps = new PrintStream(socket.getOutputStream());
      brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String tip = "";
      while (true) {
        //循环弹窗提示输入用户名
        String userName = JOptionPane.showInputDialog(tip + "请输入用户名");
        //在用户的输入前后加上协议符
        ps.println(ChatProtocol.USER_ROUND + userName + ChatProtocol.USER_ROUND);
        String result = brServer.readLine();

        if (result.equals(ChatProtocol.NAME_REP)) {
          tip = "用户名重复，请重新输入";
          continue;
        }
        if (result.equals(ChatProtocol.LOGIN_SUCCESS)) {
          break;
        }
      }
      //捕获到异常，关闭资源，退出程序
    } catch (UnknownHostException e) {
      System.out.println("找不到服务器");
      closeAllResources();
      System.exit(1);
    } catch (IOException es) {
      System.out.println("网络异常，请重试");
      closeAllResources();
      System.exit(1);
    }
    //以该Socket对应的输入流启动ClientThread线程
    new ClientThread(brServer).start();
  }

  //定义一个读取键盘输出，向网络发送的方法
  private void readAndSend() {
    try {
      String line;
      while ((line = keyIn.readLine()) != null) {
        //如果消息中含 ： 并且是//用户名 开头，则是判断为私聊
        if (line.indexOf(":") > 0 && line.startsWith("//")) {
          line = line.substring(2);
          ps.println(ChatProtocol.PRIVATE_ROUND + line.split(":")[0] + ChatProtocol.SPLIT_SIGN
              + line.split(":")[1] + ChatProtocol.PRIVATE_ROUND);
        } else {
          ps.println(ChatProtocol.MSG_ROUND + line + ChatProtocol.MSG_ROUND);
        }
      }
    } catch (Exception io) {
      System.out.println("网络异常，请重试");
      closeAllResources();
      System.exit(1);
    }
  }

  //关闭所有资源
  private void closeAllResources() {
    try {

      ResourceTool.close(keyIn, brServer, ps, socket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
