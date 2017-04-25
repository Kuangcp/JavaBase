package com.net.socket.onechatone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Myth on 2017/4/3 0003
 */
public class ServerThread extends Thread{
    private Socket socket;
    BufferedReader br = null;
    PrintStream ps = null;

    public ServerThread(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());
            String line = null;
            while((line=br.readLine())!=null){
                //如果读到的行以标识符 USER_ROUND 开始的，并以这个结束，就可以确定读到的是用户登录的登录名
                if(line.startsWith(ChatProtocol.USER_ROUND) && line.endsWith(ChatProtocol.USER_ROUND)){
                    String userName = getRealMsg(line);
                    if(Server.clients.map.containsKey(userName)){
                        System.out.println("用户名重复");
                        ps.println(ChatProtocol.NAME_REP);
                    }else{
                        System.out.println("成功");
                        ps.println(ChatProtocol.LOGIN_SUCCESS);
                        Server.clients.put(userName,ps);
                    }
                //如果标记为私聊信息，私聊信息只向特定的输出流发送
                }else if(line.startsWith(ChatProtocol.PRIVATE_ROUND) && line.endsWith(ChatProtocol.PRIVATE_ROUND)){
                    String userAndMsg = getRealMsg(line);
                    //正则信息，分出用户，信息
                    String user = userAndMsg.split(ChatProtocol.SPLIT_SIGN)[0];
                    String msg = userAndMsg.split(ChatProtocol.SPLIT_SIGN)[1];
                    //获取私聊用户对应的输出流，发送私聊信息
                    Server.clients.map.get(user).println(Server.clients.getKeyByValue(ps)+"悄悄对你说 : "+msg);
                //公聊就对每个Socket发送
                }else{
                    //得到真实消息
                    String msg = getRealMsg(line);
                    //遍历所有输出流
                    for (PrintStream clientPs : Server.clients.valueSet()) {
                        clientPs.println(Server.clients.getKeyByValue(ps)+" 说 "+msg);
                    }

                }

            }
        //有异常说明 Socket对应的客户端出现了问题 就要删除它
        }catch (Exception e){
            Server.clients.removeByValue(ps);
            System.out.println(Server.clients.map.size());
            try{
                if(br!=null) br.close();
                if(ps!=null) ps.close();
                if(socket!=null) socket.close();
            }catch (Exception es){
                es.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    //去除标识符,得到真实的信息
    private String getRealMsg(String line) {
        return line.substring(ChatProtocol.PROTOCOL_LEN,line.length()-ChatProtocol.PROTOCOL_LEN);
    }

}
