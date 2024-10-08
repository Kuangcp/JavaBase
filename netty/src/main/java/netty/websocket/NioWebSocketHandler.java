package netty.websocket;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-05-18 08:33
 */
@Slf4j
public class NioWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Map<Long, Channel> userMap = new ConcurrentHashMap<>();

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

//        private WebSocketServerHandshaker handShaker;
//
//    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
//        //要求Upgrade为websocket，过滤掉get/Post
//        if (!req.decoderResult().isSuccess()
//                || (!"websocket".equals(req.headers().get("Upgrade")))) {
//            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
//                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
//            return;
//        }
//
//        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
//                "ws://localhost:7094/ws", null, false);
//        handShaker = wsFactory.newHandshaker(req);
//        if (handShaker == null) {
//            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
//        } else {
//            handShaker.handshake(ctx.channel(), req);
//        }
//    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
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


    /**
     * 将路径参数转换成Map对象，如果路径参数出现重复参数名，将以最后的参数值为准
     *
     * @param uri 传入的携带参数的路径
     */
    public static Map<String, String> getParams(String uri) {
        Map<String, String> params = new HashMap<>(10);

        int idx = uri.indexOf("?");
        if (idx != -1) {
            String[] paramsArr = uri.substring(idx + 1).split("&");

            for (String param : paramsArr) {
                idx = param.indexOf("=");
                params.put(param.substring(0, idx), param.substring(idx + 1));
            }
        }

        return params;
    }

    /**
     * 获取URI中参数以外部分路径
     */
    public static String getBasePath(String uri) {
        if (uri == null || uri.isEmpty())
            return null;

        int idx = uri.indexOf("?");
        if (idx == -1)
            return uri;

        return uri.substring(0, idx);
    }


    /**
     * 唯一的一次http请求，用于升级至websocket 需要正确响应
     */
    private void httpHandShark(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        Map<String, String> params = getParams(uri);
//        log.info("客户端请求参数：{}", params);

        HttpHeaders headers = request.trailingHeaders();
        String userIdStr = params.get("userId");
        if (StringUtils.isBlank(userIdStr)) {
            userIdStr = headers.get("userId");
        }
        if (StringUtils.isNotBlank(userIdStr)) {
            long userId = Long.parseLong(userIdStr);
            userMap.put(userId, ctx.channel());
        }

        // 判断请求路径是否跟配置中的一致
        if (Const.webSocketPath.equals(getBasePath(uri))) {
            // 因为有可能携带了参数，导致客户端一直无法返回握手包，因此在校验通过后，重置请求路径
            request.setUri(Const.webSocketPath);
        } else {
            throw new WebSocketHandshakeException("invalid hand shark");
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof PingWebSocketFrame) {
            pingWebSocketFrameHandler(ctx, (PingWebSocketFrame) frame);
        } else if (frame instanceof TextWebSocketFrame) {
            textWebSocketFrameHandler(ctx, (TextWebSocketFrame) frame);
        } else if (frame instanceof CloseWebSocketFrame) {
            closeWebSocketFrameHandler(ctx, (CloseWebSocketFrame) frame);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("客户端请求数据类型：{}", msg.getClass());
        if (msg instanceof FullHttpRequest) {
            httpHandShark(ctx, (FullHttpRequest) msg);
        }
        super.channelRead(ctx, msg);
    }


    /**
     * 客户端发送断开请求处理
     *
     * @param ctx
     * @param frame
     */
    private void closeWebSocketFrameHandler(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        ctx.close();
    }

    /**
     * 创建连接之后，客户端发送的消息都会在这里处理
     *
     * @param ctx
     * @param frame
     */
    private void textWebSocketFrameHandler(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        ctx.channel().writeAndFlush(frame.retain());
    }

    /**
     * 处理客户端心跳包
     */
    private void pingWebSocketFrameHandler(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
    }
}
