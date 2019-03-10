package com.github.kuangcp.selfclose;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Myth on 2017/4/3
 *  Socket 半关闭状态，可以输入输出流单独控制关闭，但是关闭后就不能开启了
 *  只适用于一站式的通信协议，例如HTTP协议-客户端连接到服务器后，开始发送请求数据，发送完成后无需再次发送数据
 *  客户端，使用chattingroom下的客户端即可进行测试验证
 */
public class SocketSelfClose {
    public static void main(String []strings) throws Exception{
        ServerSocket serverSocket = new ServerSocket(30000);
        Socket socket = serverSocket.accept();
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("服务器第一行数据");
        printStream.println("服务器第二行数据");

        //关闭Socket的输出流，
        socket.shutdownOutput();
        System.out.println("socket对象是否关闭"+socket.isClosed());
        Scanner scanner = new Scanner(socket.getInputStream());
        while(scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
        }
        scanner.close();
        socket.close();
        serverSocket.close();
    }
}
