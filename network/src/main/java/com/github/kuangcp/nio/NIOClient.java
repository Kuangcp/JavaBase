package com.github.kuangcp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/4/5 0005
 */
@Slf4j
class NIOClient {

  private Selector selector = null;

  private boolean stop = false;

  public static void main(String[] s) throws Exception {
    new NIOClient().init();
  }

  private void init() throws IOException {
    selector = Selector.open();
    InetSocketAddress address = new InetSocketAddress(NIOServer.PORT);
    SocketChannel channel = SocketChannel.open(address);
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_READ);

    new ClientThread().start();

    Scanner scan = new Scanner(System.in);
    try {
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        if ("exit".equalsIgnoreCase(line)) {
          log.info("exit client");
          this.stop();
          return;
        }

        channel.write(NIOServer.charset.encode(line));
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      this.stop();
    }
  }

  private void stop() {
    this.stop = true;
  }

  //读取服务器数据的线程
  class ClientThread extends Thread {

    public void run() {
      try {
        while (!stop) {
          // 如果不设置超时时间会一直阻塞等待，导致客户端无法退出
          selector.select(5000);
          for (SelectionKey sk : selector.selectedKeys()) {
            selector.selectedKeys().remove(sk);
            if (sk.isReadable()) {
              readContent(sk);
            }
          }
        }
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

  private void readContent(SelectionKey sk) throws IOException {
    SocketChannel sc = (SocketChannel) sk.channel();
    ByteBuffer buff = ByteBuffer.allocate(1024);
    StringBuilder content = new StringBuilder();
    while (sc.read(buff) > 0) {
      sc.read(buff);
      buff.flip();
      content.append(NIOServer.charset.decode(buff));
    }
    if (content.length() == 0) {
      return;
    }
    log.info("server: {}", content);
    sk.interestOps(SelectionKey.OP_READ);
  }
}
