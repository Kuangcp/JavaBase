package com.github.kuangcp.nio.buffer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class BufferTest {

    /**
     * flip 方法直译就是翻转的意思, 也就是说在读写之间切换, 做好准备工作
     * 如果没有在读写之间调用flip会导致读到错误的数据或者抛出异常 (下标状态维护错误)
     */
    @Test
    public void testFlip() throws Exception {
        validateIntBuffer(IntBuffer.allocate(16));

        System.out.println("end");

        // 其他类型的Buffer 同理
        CharBuffer buf = CharBuffer.allocate(16);
        buf.flip();
    }

    @Test
    public void testDirect() throws Exception {
        // CharBuffer 子类
        // *U suffix for big-endian systems.
        // *S suffix for other kinds (little-endian).
        // *R suffix for read-only buffers.
        CharBuffer charBuffer = ByteBuffer.allocateDirect(16).asCharBuffer();
        System.out.println(charBuffer.getClass());

        IntBuffer intBuffer = ByteBuffer.allocateDirect(16).asIntBuffer();
        System.out.println(intBuffer.getClass());
    }

    @Test
    public void testDirectFlip() throws Exception {
        // 注意从ByteBuffer转为对应类型的Buffer时 要按类型的字节宽度重新计算容量
        IntBuffer buffer = ByteBuffer.allocateDirect(16 * 4).asIntBuffer();
        validateIntBuffer(buffer);
    }

    private static void validateIntBuffer(IntBuffer buffer) {
        // 读写都是 position -> limit 读或写超过limit就会抛 BufferOverflowException 异常
        // 写时 limit是buffer的容量
        // 读时 limit是上一次写入的position值

        // 通常来说是读写之间flip，也可以任意flip flip做了索引的置换
        for (int i = 0; i < 8; i++) {
            buffer.put(i);
        }

        // 此时 position 8 limit 16
        // 如果不做flip的话 继续读9 10 读到的就都是0了
        buffer.flip();
        // flip后 position 0 limit 8
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            int tmp = buffer.get();
            System.out.println(tmp);
            sum += tmp;
        }
        assertThat(sum, equalTo(15));

        // 同样的如果不做flip position为旧值7 因此put的1会覆盖掉前文写入的6
        buffer.flip();
        // flip 后写入position的0位置是1
        buffer.put(5);
        buffer.put(3);

        buffer.flip();
        assertThat(buffer.get(), equalTo(5));
        assertThat(buffer.get(), equalTo(3));

        // 同样的可以利用 flip 做重复读取.
        buffer.flip();
        assertThat(buffer.get(), equalTo(5));
        assertThat(buffer.get(), equalTo(3));
    }
}
