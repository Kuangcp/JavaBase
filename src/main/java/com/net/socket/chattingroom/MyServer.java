package com.net.socket.chattingroom;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Myth on 2017/4/2 0002
 * 服务器端
 * 使用多线程来是实现简单的CS架构的公共聊天室
 */
public class MyServer {
    //定义保存所有的socket的集合，并包装成线程安全的
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());
    public static void main(String []s) throws Exception{
        ServerSocket server = new ServerSocket(30000);
        while(true){
            Socket ss = server.accept();
            socketList.add(ss);
            System.out.println("建立一个连接");
            //每当客户端连接后启动一个ServerThread 线程为该客户服务
            new Thread(new ServerThread(ss)).start();
        }
    }
}
