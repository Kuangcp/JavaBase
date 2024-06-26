package netty.halfclose;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import netty.timeServer.Command;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Kuangcp
 * 2024-05-30 16:19
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private ChannelHandlerContext ctx;

    /**
     * 当客户端和服务端成功建立连接后, 就会调用该方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("connected");
        if (Objects.isNull(this.ctx)) {
            this.ctx = ctx;
        }
        sendMsg("Connected");
    }

    public boolean isConnected() {
        return Objects.nonNull(ctx);
    }

    /**
     * 当服务端回应消息时,该方法被调用
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取服务端发来的回应消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);

        log.info("client receive msg: {}", body);

        if (body.startsWith(Command.server_close)) {
            log.info("收到服务端主动关闭请求");
            sendMsg(Command.server_close_ack + " OK");
        } else if (body.startsWith(Command.client_close_ack)) {
            log.info("收到服务端的 被动关闭确认");
        }
    }

    /**
     * 当发生异常时,调用该方法
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("Unexpected exception from downstream: {}", cause.getMessage());
        ctx.close();
    }

    /**
     * 这里就需要考虑和服务端协商 发送数据的正确反序列化问题了，也就是伪TCP问题: "拆包"
     */
    public void sendMsg(String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            return;
        }

        if (Objects.isNull(ctx)) {
            log.warn("not connect");
            return;
        }

        log.info("msg={}", msg);
        byte[] req = msg.getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        // 将消息发送至服务端
        ctx.writeAndFlush(firstMessage);
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void close() {
        this.ctx.close();
    }
}
