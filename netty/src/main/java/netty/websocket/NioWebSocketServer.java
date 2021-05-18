package netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-05-18 08:32
 */
@Slf4j
public class NioWebSocketServer {

  private void init() {
    log.info("正在启动websocket服务器");
    NioEventLoopGroup boss = new NioEventLoopGroup();
    NioEventLoopGroup work = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(boss, work);
      bootstrap.channel(NioServerSocketChannel.class);
      bootstrap.childHandler(new NioWebSocketChannelInitializer());
      Channel channel = bootstrap.bind(7094).sync().channel();
      log.info("webSocket服务器启动成功：" + channel);
      channel.closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
      log.info("运行出错：" + e);
    } finally {
      boss.shutdownGracefully();
      work.shutdownGracefully();
      log.info("websocket服务器已关闭");
    }
  }

  public static void main(String[] args) {
    new NioWebSocketServer().init();
  }
}

