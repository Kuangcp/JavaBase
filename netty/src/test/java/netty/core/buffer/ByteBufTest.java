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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-25 23:06
 * https://blog.csdn.net/usagoole/article/details/88024517
 * @see com.github.kuangcp.nio.buffer.BufferTest JDK
 */
@Slf4j
public class ByteBufTest {

    @Test
    public void testCreate() throws Exception {
        // heap memory
        Unpooled.buffer(10);
        ByteBuffer.allocate(10);

        // direct memory
        ByteBufAllocator.DEFAULT.buffer(10);
        ByteBuffer.allocateDirect(10);
    }

    @Test
    public void testReadWriteInt() throws Exception {
        ByteBuf buf = Unpooled.buffer(16);
        buf.writeInt(5);
        buf.writeInt(4);

        assertThat(buf.getInt(0), equalTo(5));

        // index指数组下标，读4位字节导致读的字节数组为 0050 转为二进制的数值 就是 101 00000000 也就是1280
        assertThat(buf.getInt(1), equalTo(1280));
        assertThat(buf.getInt(2), equalTo(327680));
        assertThat(buf.getInt(3), equalTo(83886080));

        // 偏移下标和写入数据宽度对齐了，也就正确读到了第二个写入的int
        assertThat(buf.getInt(4), equalTo(4));
        assertThat(buf.getInt(5), equalTo(1024));
    }

    @Test
    public void testReadWrite() throws Exception {
        // 读写可以混用，使用不同的游标来标识
        ByteBuf buf = Unpooled.buffer(16);
        for (int i = 0; i < 3; i++) {
            buf.writeByte(i);
        }
        assertThat(buf.readByte(), equalTo((byte) 0));
        assertThat(buf.readByte(), equalTo((byte) 1));
        buf.writeByte(44);
        buf.writeByte(66);
        assertThat(buf.readByte(), equalTo((byte) 2));
        assertThat(buf.readByte(), equalTo((byte) 44));

        // 读写的index都置为0
        buf.clear();

        buf.writeByte(23);
        assertThat(buf.readByte(), equalTo((byte) 23));

        System.out.println("end");
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
