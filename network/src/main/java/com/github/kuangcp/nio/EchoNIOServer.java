package com.github.kuangcp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kuangcp on 2019-04-20 10:18 AM
 */
public class EchoNIOServer {

  private volatile boolean stop = false;

  public static void main(String[] args) throws IOException {
    new EchoNIOServer().start();
  }

  public void start() throws IOException {
    Selector selector = Selector.open();
    //通过OPEN方法来打开一个未绑定的ServerSocketChannel 实例
    ServerSocketChannel server = ServerSocketChannel.open();
    //将该ServerSocketChannel绑定到指定ip
    server.bind(new InetSocketAddress(NIOServer.PORT));
    //设置是NIO 非阻塞模式
    server.configureBlocking(false);

    //将sever注册到指定Selector对象上
    server.register(selector, SelectionKey.OP_ACCEPT);

    while (!stop) {
      selector.select(2000);
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> it = selectedKeys.iterator();
      SelectionKey key;
      while (it.hasNext()) {
        key = it.next();
        it.remove();
        try {
          handleInput(selector, key);
        } catch (Exception e) {
          if (key != null) {
            key.cancel();
            if (key.channel() != null) {
              key.channel().close();
            }
          }
        }
      }
    }
  }

  private void handleInput(Selector selector, SelectionKey key) throws IOException {
    if (key.isValid()) {
      if (key.isAcceptable()) {
        // Accept the new connection
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        // Add the new connection to the selector
        sc.register(selector, SelectionKey.OP_READ);
      }

      if (key.isReadable()) {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int readBytes = sc.read(readBuffer);
        if (readBytes > 0) {
          // 读取完就要切换
          readBuffer.flip();
          byte[] bytes = new byte[readBuffer.remaining()];
          readBuffer.get(bytes);
          String body = new String(bytes, StandardCharsets.UTF_8);
          System.out.println("The time server receive order : " + body);
          if ("STOP".equalsIgnoreCase(body)) {
            this.stop();
          }
          this.doWrite(sc, body);
        } else if (readBytes < 0) {
          // 对端链路关闭
          key.cancel();
          sc.close();
        }
        // 读到0字节，忽略
      }
    }

  }

  private void doWrite(SocketChannel channel, String body) throws IOException {
    byte[] bytes = (body + "?").getBytes();
    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
    writeBuffer.put(bytes);
    writeBuffer.flip();
    channel.write(writeBuffer);
  }

  private void stop() {
    this.stop = true;
  }
}
