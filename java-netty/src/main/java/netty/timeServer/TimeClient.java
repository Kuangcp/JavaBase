package netty.timeServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeClient {

  void connectLocal(int port) throws Exception {
    connect(port, "127.0.0.1");
  }

  void connect(int port, String host) throws Exception {
    // 配置客户端NIO线程组
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group).channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(new TimeClientHandler());
              log.info("add new client handler");
            }
          });

      // 发起异步连接操作
      ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

      // 当前客户端链路关闭
      channelFuture.channel().closeFuture().sync();
    } finally {
      // 优雅退出，释放NIO线程组
      group.shutdownGracefully();
    }
  }
}
