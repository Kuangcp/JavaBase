package netty.halfclose;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import netty.timeServer.Command;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kuangcp
 * 2024-05-30 16:17
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final AtomicInteger counter = new AtomicInteger();
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    /**
     * 成功建立连接后, 读取服务端的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);

        if (Objects.equals(body, "Connected")) {
            log.info("prepare");
            tryCloseConnect(ctx);
//            tryCloseConnectWithSleep(ctx);
            return;
        }

        log.info("server receive msg: body={} count={}", body, counter.incrementAndGet());

        String result;
        if (Command.QUERY_TIME.equalsIgnoreCase(body)) {
            result = LocalDateTime.now().toString();
        } else if (body.startsWith(Command.server_close_ack)) {
            result = "收到 被动关闭确认";
        } else if (body.startsWith(Command.client_close)) {
            result = "收到 主动关闭请求";
        } else {
            result = "BAD ORDER";
        }
        log.info("send: {}", result);
        ByteBuf response = Unpooled.copiedBuffer(result.getBytes());
        ctx.write(response);
    }

    /**
     * 可能存在的问题 还没收到客户端的关闭确认消息 就关闭了连接
     */
    private void tryCloseConnect(ChannelHandlerContext ctx) {
        // 客户端建立连接了，延迟主动关闭
        pool.schedule(() -> {
            log.info("server close");
            ByteBuf response = Unpooled.copiedBuffer(Command.server_close.getBytes());
            ctx.channel().write(response);
            ctx.channel().flush();

            // 立即关闭
            ctx.channel().close();
        }, 10, TimeUnit.SECONDS);
    }

    /**
     * 阻塞等待 响应
     */
    private void tryCloseConnectWithSleep(ChannelHandlerContext ctx) {
        // 客户端建立连接了，延迟主动关闭
        pool.schedule(() -> {
            log.info("server close");
            ByteBuf response = Unpooled.copiedBuffer(Command.server_close.getBytes());
            ctx.channel().write(response);
            ctx.channel().flush();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            // 立即关闭 可能存在的问题 还没收到关闭确认消息
            ctx.channel().close();
        }, 10, TimeUnit.SECONDS);
    }
}
