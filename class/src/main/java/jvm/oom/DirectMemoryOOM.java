package jvm.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * -XX:NativeMemoryTracking=detail
 * <p>
 * 查看内存分布jcmd pid VM.native_memory detail
 * <p>
 * TODO Java11 sun包成为内置包, 需要找到替代方式获取直接内存
 * <p>
 * https://www.jianshu.com/p/2d8bc4d4c181
 * <p>
 * https://tech.meituan.com/2019/01/03/spring-boot-native-memory-leak.html
 *
 * @author kuangcp on 4/4/19-12:29 AM
 */
public class DirectMemoryOOM {

    private static final int mib = 1024 * 1024;

    // 注意： -XX:MaxDirectMemorySize参数只对由DirectByteBuffer分配的内存有效，对Unsafe直接分配的内存无效

    /**
     * C语言malloc申请的也是虚拟内存,没有设置值的话操作系统不会分配物理内存
     *
     * @see java.nio.DirectByteBuffer#DirectByteBuffer(int)
     * <p>
     * https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html
     */
    static void byUnsafe() throws IllegalAccessException, InterruptedException {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);

        int delta = 0;
        Unsafe unsafe = (Unsafe) field.get(null);
        while (true) {
            Thread.sleep(100);
            delta += 2;
            System.out.println("now " + delta);
            // byte
            unsafe.allocateMemory(2 * mib);
        }
    }

    static void byBuffer() throws InterruptedException {
        int delta = 0;
        List<ByteBuffer> buffers = new ArrayList<>();
        while (true) {
            Thread.sleep(100);
            delta += 2;
            System.out.println("now " + delta);
            // byte
            ByteBuffer buf = ByteBuffer.allocateDirect(2 * mib);
            buf.putLong(2L);
            buf.flip();
            buffers.add(buf);
        }
    }
}
