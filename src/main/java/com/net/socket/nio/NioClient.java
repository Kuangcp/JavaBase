package com.net.socket.nio;

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
    static final int PORT = 30000;
    private Charset charset = Charset.forName("UTF-8");
    private SocketChannel sc = null;

    public static void main(String []s)throws Exception{
        new NioClient().init();
    }

    public void init()throws Exception{
        selector = Selector.open();
        InetSocketAddress isa = new InetSocketAddress("127.0.0.1",PORT);
        sc = SocketChannel.open(isa);
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        new ClientThread().start();
        Scanner scan = new Scanner(System.in);
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            sc.write(charset.encode(line));
        }

    }
    //定义读取服务器数据的线程
    private class ClientThread extends Thread{
        public void run(){
            try{
                while (selector.select()>0){
                    for(SelectionKey sk:selector.selectedKeys()){
                        selector.selectedKeys().remove(sk);
                        if(sk.isReadable()){
                            SocketChannel sc = (SocketChannel)sk.channel();
                            ByteBuffer buff = ByteBuffer.allocate(1024);
                            String content = "";
                            while(sc.read(buff)>0){
                                sc.read(buff);
                                buff.flip();
                                content +=charset.decode(buff);
                            }
                            System.out.println("聊天信息"+content);
                            sk.interestOps(SelectionKey.OP_READ);
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
