package com.github.kuangcp.chattingroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Myth on 2017/4/2 0002
 */
public class ClientThread implements Runnable{

    Socket s;
    BufferedReader br = null;

    public ClientThread(Socket s) throws Exception{
        this.s = s;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));

    }

    @Override
    public void run() {
        try{
            String content = null;
            //不断读取输入流中内容并打印输出
            while((content = br.readLine())!=null){
                System.out.println(content);
            }
        }catch (Exception r){
            r.printStackTrace();
        }
    }
}
