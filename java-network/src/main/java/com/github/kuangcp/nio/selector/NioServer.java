package com.github.kuangcp.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by Myth on 2017/4/3 0003
 * NIO Server端，节省线程
 * 该程序启动就建立了一个可监听连接请求的ServerSocketChannel，并将该Channel注册到指定的Selector
 *
 * 从JDK1.4开始，Java提供了NIO API来提供开发
 * 之前的都是一个线程处理一个客户端，线程资源消耗大，因为之前那些处理方式在程序输入输出时会线程阻塞，NIO就不会
 * NIO 采用多路复用和轮询的机制, 将阻塞都让一个线程来处理,然后提取出准备好的IO来操作, 提高性能, 请求和线程比为 n:1
 * 原先的BIO是 1:1
 */
public class NioServer {

  static final int PORT = 30000;
  //定义实现编码，解码的字符集
  private Charset charset = Charset.forName("UTF-8");
  private volatile boolean stop = false;

  public static void main(String[] s) throws Exception {
    new NioServer().init();
//        new NioServer().initByOtherWay();
  }


  // selector 模型 轮询
  private synchronized void init() throws Exception {
    Selector selector = Selector.open();

    //通过OPEN方法来打开一个未绑定的ServerSocketChannel 实例
    ServerSocketChannel server = ServerSocketChannel.open();

    //将该ServerSocketChannel绑定到指定 ip 端口
    server.bind(new InetSocketAddress(PORT));

    //设置是NIO 非阻塞模式
    server.configureBlocking(false);

    //将sever注册到指定Selector对象上
    server.register(selector, SelectionKey.OP_ACCEPT);
    // 只要有管道准备就绪了,就继续轮询
//        while (selector.select() > 0) {
    while (!stop) {
      // TODO 能写成死循环, 但是下面这个方法一定要调用, 为什么
      // 因为只有在选择了至少一个通道后，才会返回该选择器的唤醒方法，或者当前线程中断，以先到者为准。 这是API的翻译
      int result = selector.select();
      if (result <= 0) {
        continue;
      }
      System.out.println("准备好的数量:" + result);
      //依次处理selector上的每个已经准备好的管道
      for (SelectionKey sk : selector.selectedKeys()) {
        //从selector上的已选择key集中删除正在处理的连接请求
        selector.selectedKeys().remove(sk);
        if (sk.isAcceptable()) {
          //调用accept方法，产生服务器端的SocketChannel
          SocketChannel sc = server.accept();
          sc.configureBlocking(false);//NIO模式
          sc.register(selector, SelectionKey.OP_READ);//注册到selector上
          sk.interestOps(SelectionKey.OP_ACCEPT);//将sk对应的Channel设置成准备接受其他请求
        }
        //如果有数据需要读取
        // TODO 为什么 打开服务端,打开客户端, 然后关闭客户端, 服务端死循环的输出,再次打开客户端后 ,
        // 服务端就报错 ConcurrentModificationException 退出了, 客户端也随之断开了连接
        // 当给方法加上同步关键字后, 就直接退出了没有报错, 说明, 当客户端重启后, 存在并发修改
        // TODO 那么为什么呢
        // 反之则是客户端死循环输出
        if (sk.isReadable()) {
          SocketChannel sc = (SocketChannel) sk.channel();
          ByteBuffer buff = ByteBuffer.allocate(1024);

          StringBuilder content = new StringBuilder();
          try {
            while (sc.read(buff) > 0) {
              buff.flip();
              content.append(charset.decode(buff));
            }
            System.out.println("读取的数据 : " + content.toString());
            if ("stop".equalsIgnoreCase(content.toString())) {
              stop();
            }
            sk.interestOps(SelectionKey.OP_READ);//设置成准备下次读取
          } catch (Exception e) {
            //从Selector中删除指定的SelectionKey
            sk.cancel();
            if (sk.channel() != null) {
              sk.channel().close();
            }
          }
          //聊天信息不为空
          if (content.length() > 0) {
            //遍历selector里注册的所有SelectionKey
            for (SelectionKey key : selector.keys()) {
              Channel targetChannel = key.channel();//获取Channel
              //如果改Channel是SocketChannel是SocketChannel对象
              if (targetChannel instanceof SocketChannel) {
                //将读到的内容写入到该Channel中去
                SocketChannel dest = (SocketChannel) targetChannel;
                dest.write(charset.encode(content.toString()));
              }
            }
          }
        }
      }

    }

  }

  private void stop() {
    this.stop = true;
  }

//
//    public void initByOtherWay() throws IOException {
//        Selector selector = Selector.open();
//        //通过OPEN方法来打开一个未绑定的ServerSocketChannel 实例
//        ServerSocketChannel server = ServerSocketChannel.open();
//        //将该ServerSocketChannel绑定到指定ip
//        server.bind(new InetSocketAddress(PORT));
//        //设置是NIO 非阻塞模式
//        server.configureBlocking(false);
//
//        //将sever注册到指定Selector对象上
//        server.register(selector, SelectionKey.OP_ACCEPT);
//
//        while (!stop) {
//            selector.select(2000);
//            Set<SelectionKey> selectedKeys = selector.selectedKeys();
//            Iterator<SelectionKey> it = selectedKeys.iterator();
//            SelectionKey key;
//            while (it.hasNext()) {
//                key = it.next();
//                it.remove();
//                try {
//                    handleInput(key);
//                } catch (Exception e) {
//                    if (key != null) {
//                        key.cancel();
//                        if (key.channel() != null)
//                            key.channel().close();
//                    }
//                }
//            }
//        }
//    }
//
//    private void handleInput(SelectionKey key) throws IOException {
//        if(key.isValid()){
//            if(key.isAcceptable()){
//                // Accept the new connection
//                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
//                SocketChannel sc = ssc.accept();
//                sc.configureBlocking(false);
//                // Add the new connection to the selector
//                sc.register(selector, SelectionKey.OP_READ);
//            }
//            if(key.isReadable()){
//                SocketChannel sc = (SocketChannel) key.channel();
//                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//                int readBytes = sc.read(readBuffer);
//                if (readBytes > 0) {
//                    // 读取完就要切换
//                    readBuffer.flip();
//                    byte[] bytes = new byte[readBuffer.remaining()];
//                    readBuffer.get(bytes);
//                    String body = new String(bytes, "UTF-8");
//                    System.out.println("The time server receive order : "+ body);
//                    if("STOP".equalsIgnoreCase(body)){
//                        stop();
//                    }
//                    doWrite(sc, "response");
//                } else if (readBytes < 0) {
//                    // 对端链路关闭
//                    key.cancel();
//                    sc.close();
//                }
//                // 读到0字节，忽略
//            }
//        }
//
//    }
//

//
//    private void doWrite(SocketChannel channel, String response)
//            throws IOException {
//        if (response != null && response.trim().length() > 0) {
//            byte[] bytes = response.getBytes();
//            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
//            writeBuffer.put(bytes);
//            writeBuffer.flip();
//            channel.write(writeBuffer);
//        }
//    }


}
