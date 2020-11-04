package jvm.oom;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * -Xmx20M -XX:MaxDirectMemorySize=10M
 *
 * TODO 瞬间内存占满 似乎该参数在8中失效了
 * TODO Java11 sun包成为内置包, 需要找到替代方式获取直接内存
 *
 * @author kuangcp on 4/4/19-12:29 AM
 */
public class DirectMemoryOOM {

  private static final int memoryBlock = 1024 * 1024;

  public static void main(String[] args) throws IllegalAccessException {
    Field field = Unsafe.class.getDeclaredFields()[0];
    field.setAccessible(true);
    Unsafe unsafe = (Unsafe) field.get(null);
    while (true) {
      unsafe.allocateMemory(memoryBlock);
    }
  }
}
