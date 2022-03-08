package netty.websocket;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-05-18 08:33
 */
@Slf4j
public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {

  private WebSocketServerHandshaker handshaker;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    log.debug("收到消息：" + msg);
    if (msg instanceof FullHttpRequest) {
      //以http请求形式接入，但是走的是websocket
      handleHttpRequest(ctx, (FullHttpRequest) msg);
    } else if (msg instanceof WebSocketFrame) {
      //处理websocket客户端的消息
      handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.debug("客户端加入连接：" + ctx.channel());
    ChannelSupervise.addChannel(ctx.channel());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.debug("客户端断开连接：" + ctx.channel());
    ChannelSupervise.removeChannel(ctx.channel());
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    // 判断是否关闭链路的指令
    if (frame instanceof CloseWebSocketFrame) {
      handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
      return;
    }

    // 判断是否ping消息
    if (frame instanceof PingWebSocketFrame) {
      ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
      return;
    }

    // 本例程仅支持文本消息，不支持二进制消息
    if (!(frame instanceof TextWebSocketFrame)) {
      log.debug("本例程仅支持文本消息，不支持二进制消息");
      throw new UnsupportedOperationException(String.format(
          "%s frame types not supported", frame.getClass().getName()));
    }

    // 返回应答消息
    String request = ((TextWebSocketFrame) frame).text();
    log.debug("服务端收到：" + request);
    TextWebSocketFrame tws = new TextWebSocketFrame(
        new Date().toString() + ctx.channel().id() + "：" + request);

    // 群发至所有连接
//    ChannelSupervise.send2All(tws);

    // 返回【谁发的发给谁】
    ctx.channel().writeAndFlush(tws);
  }

  /**
   * 唯一的一次http请求，用于创建websocket
   */
  private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
    //要求Upgrade为websocket，过滤掉get/Post
    if (!req.decoderResult().isSuccess()
        || (!"websocket".equals(req.headers().get("Upgrade")))) {
      //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
      sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
          HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
      return;
    }

    WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
        "ws://localhost:7094/ws", null, false);
    handshaker = wsFactory.newHandshaker(req);
    if (handshaker == null) {
      WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
    } else {
      handshaker.handshake(ctx.channel(), req);
    }
  }

  /**
   * 拒绝不合法的请求，并返回错误信息
   */
  private static void sendHttpResponse(ChannelHandlerContext ctx,
      FullHttpRequest req, DefaultFullHttpResponse res) {
    // 返回应答给客户端
    if (res.status().code() != 200) {
      ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
      res.content().writeBytes(buf);
      buf.release();
    }
    ChannelFuture f = ctx.channel().writeAndFlush(res);
    // 如果是非Keep-Alive，关闭连接
    if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
      f.addListener(ChannelFutureListener.CLOSE);
    }
  }
}
