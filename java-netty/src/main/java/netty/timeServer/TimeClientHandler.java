package netty.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.logging.Logger;

/**
 * 客户端的业务代码 继承自 ChannelHandlerAdapter
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    /**
     * 当客户端和服务端成功建立连接后, 就会调用该方法
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        // 将消息发送至服务端
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端回应消息时,该方法被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取服务端发来的回应消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("收到的时间: " + body);
    }

    /**
     * 当发生异常时,调用该方法
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 释放资源
        logger.warning("Unexpected exception from downstream : "+ cause.getMessage());
        ctx.close();
    }
}
