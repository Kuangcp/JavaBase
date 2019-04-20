package netty.timeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

  static final int port = 8080;

  public void start() throws Exception {
    // NIO 结合两个线程池
    // 配置服务端的NIO线程组 , 一个用于接受客户端的连接, 一个是处理SocketChannel的网络读写
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 512)
          .childHandler(new ChildChannelHandler());
      // 绑定端口，同步等待成功 返回一个ChannelFuture, 用于异步操作的通知回调
      ChannelFuture f = serverBootstrap.bind(port).sync();

      // 等待服务端监听端口关闭
      f.channel().closeFuture().sync();
    } finally {
      // 优雅退出，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel arg0) {
      arg0.pipeline().addLast(new TimeServerHandler());
    }

  }
}
