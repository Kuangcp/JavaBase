package com.github.kuangcp.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Myth on 2017/4/5 0005
 */
@Slf4j
class NIOClient {

    private volatile boolean stop = false;

    public static void main(String[] s) throws Exception {
        new NIOClient().init();
    }

    private void init() throws IOException {
        Selector selector = Selector.open();
        InetSocketAddress address = new InetSocketAddress(NIOServer.PORT);
        SocketChannel channel = SocketChannel.open(address);
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        log.info("start client");

        Thread input = new Thread(() -> readInput(channel));
        input.setDaemon(true);
        input.start();

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

    private void readInput(SocketChannel channel) {
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

    private void readContent(SelectionKey sk) throws IOException {
        StringBuilder content;
        SocketChannel sc = (SocketChannel) sk.channel();
        ByteBuffer buff = ByteBuffer.allocate(1024);
        content = new StringBuilder();
        while (sc.read(buff) > 0) {
            sc.read(buff);
            buff.flip();
            content.append(NIOServer.charset.decode(buff));
        }
        if (content.length() == 0) {
            return;
        }
        log.info("server: {}", content);
        if (Objects.equals(content.toString(), "Shutdown")) {
            this.stop();
            return;
        }
        sk.interestOps(SelectionKey.OP_READ);
    }
}
