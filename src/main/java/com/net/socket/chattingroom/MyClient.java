package com.net.socket.chattingroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Myth on 2017/4/2
 * 单线程的客户端
 */
public class MyClient {
    static String name;

    public static void main(String p[]) throws Exception{
        setName();
        Socket s = new Socket("127.0.0.1",30000);
        //客户端启动线程不断的读取来自服务器的数据
        new Thread(new ClientThread(s)).start();
        PrintStream ps = new PrintStream(s.getOutputStream());
        String line = null;
        //不断读取键盘输入
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        while((line=br.readLine())!=null){
            //将键盘输入写入Socket输入流中
            ps.println(name+" : "+line);
        }
    }
    public static void setName()throws Exception{
        System.out.println("请输入名字");
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        name = br.readLine();
        System.out.println("你申请的用户名是 : "+name);
    }
}
