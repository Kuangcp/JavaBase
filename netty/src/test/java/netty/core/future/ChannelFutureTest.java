package netty.core.future;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.local.LocalServerChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 *
 * @author Kuangcp 
 * 2024-02-01 10:43
 */
public class ChannelFutureTest {

    @Test
    public void testTcpConn() throws Exception {
        Channel channel = new LocalServerChannel();

        // 1 连接到远程 非阻塞操作
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.0.1", 25));
        // 2 注册Listener
        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                // 3. 连接成功做操作
                ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                ChannelFuture wf = f.channel().writeAndFlush(buffer);
                // ...
            } else {
                // 3. 处理异常
                Throwable cause = f.cause();
                cause.printStackTrace();
            }
        });
    }

}