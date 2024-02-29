package com.github.kuangcp.runable;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * 服务器端
 * 输入线程阻塞的原因，不能同时收发，怎么处理？多线程？
 * 服务器不应该发出，而是做中转站
 */
@Slf4j
public class GreetingServer extends Thread {

    private ServerSocket serverSocket;

    public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                log.info("#####  Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                Scanner scanner = new Scanner(System.in);

                log.info("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                log.info("接收到的：" + in.readUTF());
                // 对客户端发出的消息
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                while (true) {
                    String temp = scanner.nextLine();
                    log.info("input:" + temp);
                    out.writeUTF(temp);
                    if ("90".equals(temp)) {
                        break;
                    }

                }
                out.writeUTF(
                        "Thank you for connecting to " + server.getLocalSocketAddress() + "  Goodbye!");

                server.close();
            } catch (SocketTimeoutException s) {
                log.info("Socket timed out!");
                break;
            } catch (IOException e) {
                log.error("", e);
                break;
            }
        }
    }

    public static void main(String[] args) {
//      int port = Integer.parseInt(args[0]);
        int port = 10000;
        try {
            Thread t = new GreetingServer(port);
            t.start();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
