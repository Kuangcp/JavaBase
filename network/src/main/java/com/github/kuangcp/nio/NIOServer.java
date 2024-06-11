package com.github.kuangcp.nio;

import com.github.kuangcp.io.ResourceTool;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Created by Myth on 2017/4/3 0003
 * NIO Server端，节省线程
 * 该程序启动就建立了一个可监听连接请求的ServerSocketChannel，并将该Channel注册到指定的Selector
 * <p>
 * 从JDK1.4开始，Java提供了NIO API来提供开发
 * 之前的都是一个线程处理一个客户端，线程资源消耗大，因为之前那些处理方式在程序输入输出时会线程阻塞，NIO就不会
 * NIO 采用多路复用和轮询的机制, 将阻塞都让一个线程来处理,然后提取出准备好的IO来操作, 提高性能, 请求和线程比为 n:1
 * 原先的BIO是 1:1
 */
@Slf4j
class NIOServer {

    static final int PORT = 30000;

    //定义实现编码，解码的字符集
    static Charset charset = StandardCharsets.UTF_8;

    private volatile boolean stop = false;

    public static void main(String[] s) throws Exception {
        new NIOServer().start();
    }

    // selector 模型 轮询
    private void start() throws Exception {
        Selector selector = Selector.open();

        //通过OPEN方法来打开一个未绑定的ServerSocketChannel 实例
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(PORT));
        //设置是NIO 非阻塞模式
        server.configureBlocking(false);

        //将sever注册到指定Selector对象上
        server.register(selector, SelectionKey.OP_ACCEPT);
        log.info("start server");

        while (!stop) {
            // 因为只有在选择了至少一个通道后，才会返回该选择器的唤醒方法，或者当前线程中断，以先到者为准。否则一直阻塞
            selector.select();

            //依次处理selector上的每个已经准备好的管道
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for (SelectionKey sk : selectedKeys) {
                //从selector上的已选择key集 中删除正在处理的连接请求
                selector.selectedKeys().remove(sk);

                // 连接 accept
                if (sk.isAcceptable()) {
                    register(selector, server, sk);
                }
                // 连接建立
                else if (sk.isConnectable()) {
                    log.info("connected");
                }
                // 读就绪
                else if (sk.isReadable()) {
                    readContent(selector, sk);
                }
                // 写就绪
                else if (sk.isWritable()) {
                    log.info("writeable");
                }
            }
        }
    }

    private void register(Selector selector, ServerSocketChannel server, SelectionKey sk)
            throws IOException {
        //调用accept方法，产生服务器端的SocketChannel
        SocketChannel sc = server.accept();
        sc.configureBlocking(false);//NIO模式
        sc.register(selector, SelectionKey.OP_READ);//注册到selector上
        sk.interestOps(SelectionKey.OP_ACCEPT);//将sk对应的Channel设置成准备接受其他请求
    }

    // 解析数据
    private void readContent(Selector selector, SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        ByteBuffer buff = ByteBuffer.allocate(1024);

        StringBuilder content = new StringBuilder();
        try {
            int s;
            while ((s = sc.read(buff)) > 0) {
                buff.flip();
                content.append(charset.decode(buff));
            }
            buff.clear();
            if (s == -1) {
                log.info("close");
                sc.close();
            }

            if (content.length() == 0) {
                return;
            }

            log.info("receive msg: {}", content);
            if ("stop".equalsIgnoreCase(content.toString())) {
                broadcastToClient(selector, "Shutdown");
                stop();
                log.info("stop the sever");
                return;
            }

            sk.interestOps(SelectionKey.OP_READ);//设置成准备下次读取
        } catch (Exception e) {
            //从Selector中删除指定的SelectionKey
            sk.cancel();
            ResourceTool.close(sk.channel());
        }

        if (content.length() <= 0) {
            return;
        }

        broadcastToClient(selector, content.toString());
    }

    private static void broadcastToClient(Selector selector, String content) throws IOException {
        // 遍历selector里注册的所有SelectionKey
        // 也就是广播消息出去
        for (SelectionKey key : selector.keys()) {
            //获取Channel
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel) {
                //将读到的内容写入到该Channel中去
                SocketChannel dest = (SocketChannel) targetChannel;
                dest.write(charset.encode(content));
            }
        }
    }

    private void stop() {
        this.stop = true;
    }
}
