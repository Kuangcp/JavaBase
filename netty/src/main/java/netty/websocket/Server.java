package netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 客户端使用 client.html
 * 压测可使用 https://github.com/Kuangcp/GoBase/tree/master/toolbox/web-socket
 *
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-05-18 08:32
 */
@Slf4j
public class Server {

    private void init() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(ChannelSupervise::watchState, 5, 3, TimeUnit.SECONDS);

        log.info("正在启动WebSocket服务器");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NioWebSocketChannelInitializer());
            Channel channel = bootstrap.bind(7094).sync().channel();
            log.info("WebSocket服务器启动成功：{}", channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("", e);
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("WebSocket服务器已关闭");
        }
    }

    public static void main(String[] args) {
        new Server().init();
    }
}

