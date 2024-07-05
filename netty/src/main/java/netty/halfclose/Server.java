package netty.halfclose;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kuangcp
 * 2024-05-30 16:15
 */
@Slf4j
public class Server {
    static final int port = 30085;
    private Channel channel;

    public void start() throws Exception {
        // NIO 结合两个线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // boss/acceptor 用于接受客户端的连接  worker 处理SocketChannel的 read write 然后交由对应的Handler处理
            // 可以看 group 的 doc
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .childHandler(new Server.ChannelInit());

            // 绑定端口，同步等待成功 返回一个ChannelFuture, 用于异步操作的通知回调
            ChannelFuture future = serverBootstrap.bind(port).sync();
            this.channel = future.channel();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        log.info("stop server");
        this.channel.close();
    }

    @Slf4j
    private static class ChannelInit extends ChannelInitializer<SocketChannel> {

        ServerHandler handler = new ServerHandler();

        public ChannelInit() {
            log.info("init a initializer");
        }

        @Override
        protected void initChannel(SocketChannel arg0) {
            arg0.pipeline().addLast(handler);
        }
    }
}
