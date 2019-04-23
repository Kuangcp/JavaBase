package netty.timeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

class TimeServer {

  static final int port = 8080;

  public void start() throws Exception {
    // NIO 结合两个线程池 , boss 用于接受客户端的连接
    //    worker 处理SocketChannel的 read write 然后交由对应的Handler处理
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 512)
          .childHandler(new ChildChannelHandler());

      // 绑定端口，同步等待成功 返回一个ChannelFuture, 用于异步操作的通知回调
      ChannelFuture future = serverBootstrap.bind(port).sync();

      // 等待服务端监听端口关闭
      future.channel().closeFuture().sync();
    } finally {
      // 优雅退出，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  @Slf4j
  private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    public ChildChannelHandler() {
      log.info("init a initializer");
    }

    @Override
    protected void initChannel(SocketChannel arg0) {
      arg0.pipeline().addLast(new TimeServerHandler());
    }
  }
}
