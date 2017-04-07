package com.net.socket.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * Created by Myth on 2017/4/3 0003
 * NIO Server端，节省线程
 * 该程序启动就建立了一个可监听连接请求的ServerSocketChannel，并将该Channel注册到指定的Selector
 * 从JDK1.4开始，Java提供了NIO API来提供开发
 * 之前的都是一个线程处理一个客户端，线程资源消耗大，因为之前那些处理方式在程序输入输出时会线程阻塞，NIO就不会
 */
public class NioServer {
    private Selector selector = null;
    static final int PORT = 30000;
    //定义实现编码，解码的字符集
    private Charset charset = Charset.forName("UTF-8");

    public static void main(String []s)throws Exception{
        new NioServer().init();
    }

    public void init()throws Exception{
        selector = Selector.open();
        //通过OPEN方法来打开一个未绑定的ServerSocketChannel 实例
        ServerSocketChannel server = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress("127.0.0.1",PORT);
        //将该ServerSocketChannel绑定到指定ip
        server.bind(isa);
        //设置是NIO 非阻塞模式
        server.configureBlocking(false);
        //将sever注册到指定Selector对象上
        server.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select()>0){
            //依次处理selector上的每个已选择的SelectionKey
            for (SelectionKey sk : selector.selectedKeys()) {
                //从selector上的已选择key集中删除正在处理的连接请求
                selector.selectedKeys().remove(sk);
                if(sk.isAcceptable()){
                    //调用accept方法，产生服务器端的SocketChannel
                    SocketChannel sc = server.accept();
                    sc.configureBlocking(false);//NIO模式
                    sc.register(selector,SelectionKey.OP_READ);//注册到selector上
                    sk.interestOps(SelectionKey.OP_ACCEPT);//将sk对应的Channel设置成准备接受请他请求
                }
                //如果有数据需要读取
                if(sk.isReadable()){
                    SocketChannel sc = (SocketChannel)sk.channel();
                    ByteBuffer buff = ByteBuffer.allocate(1024);
                    String content = "";
                    try{
                        while(sc.read(buff)>0){
                            buff.flip();
                            content+=charset.decode(buff);
                        }
                        System.out.println("读取的数据 : "+content);
                        sk.interestOps(SelectionKey.OP_READ);//设置成准备下次读取
                    }catch (Exception e){
                        //从Selector中删除指定的SelectionKey
                        sk.cancel();
                        if(sk.channel()!=null){
                            sk.channel().close();
                        }
                    }
                    //聊天信息不为空
                    if(content.length()>0){
                        //遍历selector里注册的所有SelectionKey
                        for(SelectionKey key:selector.keys()){
                            Channel targetChannel = key.channel();//获取Channel
                            //如果改Channel是SocketChannel是SocketChannel对象
                            if(targetChannel instanceof SocketChannel){
                                //将读到的内容写入到该Channel中去
                                SocketChannel dest = (SocketChannel)targetChannel;
                                dest.write(charset.encode(content));
                            }
                        }
                    }
                }
            }

        }

    }

}
