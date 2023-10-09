package com.github.kuangcp.bio.onechatone;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

/**
 * Created by Myth on 2017/4/3 0003
 */
@Slf4j
public class ClientThread extends Thread {
    final BufferedReader br;

    public ClientThread(BufferedReader br) {
        this.br = br;
    }

    public void run() {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                //显示服务器读取到的内容
                System.out.println(line);
               /*
               此处的业务可以根据需求复杂化，并不只是简单的打印输出，
               例如QQ客户端，就要在有好友上线下线时接受好友在线列表，接受群聊消息或私聊消息，就要有对应的协议字符（特殊字符不容易出错）
               例如 在线五子棋，就要传输下子的坐标，如果要聊天，又要接受消息
               需求总是提不尽的
                */
            }
        } catch (Exception r) {
            log.error("", r);
        } finally {
            try {
                if (br != null) br.close();
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
