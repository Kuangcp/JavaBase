package netty.websocket;

import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import static io.netty.handler.codec.http.HttpHeaders.Names.WEBSOCKET_PROTOCOL;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-05-18 08:33
 */
public class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * @see AdaptiveRecvByteBufAllocator.HandleImpl#record(int) 实现扩缩容读写ByteBuf
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logging", new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        pipeline.addLast("http-codec", new HttpServerCodec());//设置解码器
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());//用于大数据的分区传输
        pipeline.addLast("handler", new NioWebSocketHandler());//自定义的业务handler

        // checkStartsWith 为true 支持路径带参数
        // maxFrameSize 设置的是最大可申请的ByteBuf，实际上使用时是按需申请和回收内存
        pipeline.addLast("", new WebSocketServerProtocolHandler(Const.webSocketPath, WEBSOCKET_PROTOCOL, true, 65536, false, true));
    }
}
