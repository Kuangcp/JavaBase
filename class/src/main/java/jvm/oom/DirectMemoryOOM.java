package jvm.oom;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import sun.misc.Unsafe;

/**
 * -XX:NativeMemoryTracking=detail
 *
 * 查看内存分布jcmd pid VM.native_memory detail
 *
 * TODO Java11 sun包成为内置包, 需要找到替代方式获取直接内存
 *
 * https://www.jianshu.com/p/2d8bc4d4c181
 *
 * https://tech.meituan.com/2019/01/03/spring-boot-native-memory-leak.html
 *
 * @author kuangcp on 4/4/19-12:29 AM
 */
public class DirectMemoryOOM {

  private static final long memoryBlock = 1024 * 1024L;

  // -XX:MaxDirectMemorySize参数只对由DirectByteBuffer分配的内存有效，对Unsafe直接分配的内存无效

  /**
   * TODO 为什么 这里分配的是虚拟内存
   */
  static void byUnsafe() throws IllegalAccessException, InterruptedException {
    Field field = Unsafe.class.getDeclaredFields()[0];
    field.setAccessible(true);

    Unsafe unsafe = (Unsafe) field.get(null);
    while (true) {
      Thread.sleep(50);
      unsafe.allocateMemory(memoryBlock);
    }
  }

  /**
   * TODO 为什么会进行回收 -XX:MaxDirectMemorySize
   */
  static void byBuffer() throws InterruptedException {
    while (true) {
      Thread.sleep(10);
      ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 1024);
      buf.putLong(2L);
      buf.flip();
    }
  }
}
