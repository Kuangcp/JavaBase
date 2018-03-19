package com.github.kuangcp.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Myth on 2017/4/5 0005
 * NIO实现的客户端，相对于普通的CS结构节省资源
 */
public class NioClient {
    //定义检测SocketChannel的Selector对象
    private Selector selector = null;
    private Charset charset = Charset.forName("UTF-8");

    private boolean stop = false;


    public static void main(String[] s) throws Exception {
        new NioClient().init();
    }

    public void init() throws Exception {
        selector = Selector.open();
        InetSocketAddress isa = new InetSocketAddress(NioServer.PORT);
        SocketChannel sc = SocketChannel.open(isa);
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        new ClientThread().start();
        Scanner scan = new Scanner(System.in);
//        while (scan.hasNextLine()) {
        while (!stop) {
            String line = scan.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                // 只是停掉了通信线程, 但是该方法的输入线程还是被阻塞了
                stop();
            }
            sc.write(charset.encode(line));
        }
    }

    private void stop() {
        this.stop = true;
    }

    //定义读取服务器数据的线程
    private class ClientThread extends Thread {
        public void run() {
            try {
                while (!stop) {
                    int result = selector.select();
                    if (result <= 0) {
                        continue;
                    }
                    System.out.println(stop + "准备好的数量:" + result);
                    for (SelectionKey sk : selector.selectedKeys()) {
                        selector.selectedKeys().remove(sk);
                        if (sk.isReadable()) {
                            SocketChannel sc = (SocketChannel) sk.channel();
                            ByteBuffer buff = ByteBuffer.allocate(1024);
                            StringBuilder content = new StringBuilder();
                            while (sc.read(buff) > 0) {
                                sc.read(buff);
                                buff.flip();
                                content.append(charset.decode(buff));
                            }
                            System.out.println("聊天信息" + content.toString());
                            sk.interestOps(SelectionKey.OP_READ);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
