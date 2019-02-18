package netty.timeServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

  public void connect(int port, String host) throws Exception {
    // 配置客户端NIO线程组
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group).channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch)
                throws Exception {
              ch.pipeline().addLast(new TimeClientHandler());
              System.out.println("添加一个");
            }
          });
      // 发起异步连接操作
      ChannelFuture f = b.connect(host, port).sync();

      // 当代客户端链路关闭
      f.channel().closeFuture().sync();
    } finally {
      // 优雅退出，释放NIO线程组
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    new TimeClient().connect(8080, "127.0.0.1");
  }
}
