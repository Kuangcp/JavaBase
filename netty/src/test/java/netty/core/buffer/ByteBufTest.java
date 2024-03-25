package netty.core.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-25 23:06
 */
@Slf4j
public class ByteBufTest {

    @Test
    public void testApiUse() throws Exception {
        // heap memory
        Unpooled.buffer(10);
        ByteBuffer.allocate(10);

        // direct memory
        ByteBufAllocator.DEFAULT.buffer(10);
        ByteBuffer.allocateDirect(10);
    }

    /**
     * 一样的分配全部申请额
     */
    @Test
    public void testAllocateMemory() throws Exception {
        final int size = 2 * 1024 * 1024;
        List<ByteBuffer> cache = new ArrayList<>();
        List<ByteBuf> cache2 = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            final ByteBuffer a = ByteBuffer.allocate(size);
            final ByteBuf b = Unpooled.buffer(size);
            cache.add(a);
            cache2.add(b);

            TimeUnit.SECONDS.sleep(1);
            log.info("allocate " + i);
        }
        TimeUnit.MINUTES.sleep(1);
    }

    @Test
    public void testAdaptiveMemory() throws Exception {
        final AdaptiveRecvByteBufAllocator allocator = new AdaptiveRecvByteBufAllocator();
        final RecvByteBufAllocator.Handle handle = allocator.newHandle();
        handle.allocate(UnpooledByteBufAllocator.DEFAULT);
        //TODO 测试读写中Debug扩缩容机制
    }
}
