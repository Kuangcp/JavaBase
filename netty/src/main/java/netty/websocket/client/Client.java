package netty.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/http/websocketx/client/WebSocketClient.java
 * @author Kuangcp
 * 2024-03-29 14:13
 */
@Slf4j
public class Client {

    static final String URL = System.getProperty("url", "ws://127.0.0.1:7094/ws");

    public static void main(String[] args) throws Exception {
        URI uri = new URI(URL);
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) protocol is supported.");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);

        List<EventLoopGroup> groupList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            EventLoopGroup group = new NioEventLoopGroup(1, pool);
            groupList.add(group);
            try {
                // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
                // If you change it to V00, ping is not supported and remember to change
                // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
                WebSocketClientHandshaker shaker = WebSocketClientHandshakerFactory.newHandshaker(
                        new URI(URL + "?userId=1"), WebSocketVersion.V13, null,
                        true, new DefaultHttpHeaders());
                final WebSocketClientHandler handler = new WebSocketClientHandler(shaker, pool);
                clientChannel(handler, group, sslCtx, host, port);
            } finally {
                group.shutdownGracefully();
            }
        }
        for (EventLoopGroup group : groupList) {
            group.shutdownGracefully();
        }
    }

    private static void clientChannel(WebSocketClientHandler handler, EventLoopGroup group, SslContext sslCtx,
                                      String host, int port) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                        }
                        p.addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                WebSocketClientCompressionHandler.INSTANCE,
                                handler);
                    }
                });

        Channel ch = b.connect(host, port).sync().channel();
        handler.handshakeFuture().sync();

//        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String msg = console.readLine();
//            if (msg == null) {
//                break;
//            } else if ("bye".equalsIgnoreCase(msg)) {
//                ch.writeAndFlush(new CloseWebSocketFrame());
//                ch.closeFuture().sync();
//                break;
//            } else if ("ping".equalsIgnoreCase(msg)) {
//                WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
//                ch.writeAndFlush(frame);
//            } else {
//                WebSocketFrame frame = new TextWebSocketFrame(msg);
//                ch.writeAndFlush(frame);
//            }
//        }
    }
}
