package com.net.socket.chattingroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Myth on 2017/4/2 0002
 * 负责处理每个线程通信的线程类
 */
public class ServerThread implements Runnable{

    //定义当前线程处理的Socket
    Socket s = null;
    //该线程处理的Socket对应的输入流
    BufferedReader br = null;
    public ServerThread(Socket s)throws IOException{
        this.s = s;
        //初始化该输入流
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));

    }
    @Override
    public void run() {
        try{
            String content = null;
            //不断的从Socket中读取客户端发送过来的数据
            while((content = readFromClient())!=null){
                System.out.println("Chat##  "+content);
                //遍历socketList中的每个Socket
                for(Socket ss:MyServer.socketList){
                    //将读到的内容向所有Socket发送，广播形式
                    PrintStream sp = new PrintStream(ss.getOutputStream());
                    sp.println(content);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //定义读取客户端数据的方法
    private String readFromClient() {
        try{
            return br.readLine();
            //如果捕获到异常，则表明该Socket对应的客户端已经关闭
        }catch (Exception r){
            //删除该Socket
            System.out.println("删除Socket");
            MyServer.socketList.remove(s);
        }
        return null;
    }
}
