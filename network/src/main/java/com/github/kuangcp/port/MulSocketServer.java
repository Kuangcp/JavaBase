package com.github.kuangcp.port;

import com.github.kuangcp.io.ResourceTool;
import com.github.kuangcp.runable.GreetingClient;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 复用连接的echo服务器
 *
 * 功能：将客户端发送的内容反馈给客户端
 *
 * @see GreetingClient 客户端
 */
@Slf4j
public class MulSocketServer {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        // 监听端口号
        int port = 10000;
        try {
            // 建立连接
            serverSocket = new ServerSocket(port);
            System.out.println("服务器已启动：");
            // 获得连接
            socket = serverSocket.accept();
            // 初始化流
            is = socket.getInputStream();
            os = socket.getOutputStream();
            byte[] b = new byte[1024];
            for (int i = 0; i < 3; i++) {
                int n = is.read(b);
                // 输出
                System.out.println("客户端发送内容为：" + new String(b, 0, n));
                // 向客户端发送反馈内容
                os.write(b, 0, n);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                ResourceTool.close(os, is, socket, serverSocket);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}