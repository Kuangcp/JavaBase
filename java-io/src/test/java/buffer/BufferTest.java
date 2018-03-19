package buffer;

import org.junit.Test;

import java.nio.IntBuffer;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-19  上午11:42
 */
public class BufferTest {

    /**
     * flip 方法直译就是翻转的意思, 也就是说在读写之间切换, 做好准备工作
     */
    @Test
    public void testFlip(){
        IntBuffer buffer = IntBuffer.allocate(1024);
        buffer.put(1);
        // 需要使用flip方法来进行模式的切换, 如果没有做读写操作就直接调用就会抛出 BufferOverflowException 异常
        // 也就是说, 在写和读操作后, 需要调用该方法才能进行下一步的写和读
        buffer.flip();
        buffer.put(2);
        buffer.flip();
        int result = buffer.get();
        System.out.println(result);
    }
}
