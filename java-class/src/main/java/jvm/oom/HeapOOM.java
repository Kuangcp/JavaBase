package jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
 * 当实例化 5 个 HeapOOM 对象后就OOM了
 *
 * @author kuangcp on 4/3/19-10:11 PM
 */
public class HeapOOM {

  private byte[] data = new byte[1024 * 1024]; // 1 mib

  public static void main(String[] args) {
    List<HeapOOM> data = new ArrayList<>();
    while (true) {
      data.add(new HeapOOM());
    }
  }
}
