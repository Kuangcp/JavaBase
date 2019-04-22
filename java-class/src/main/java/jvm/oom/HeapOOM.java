package jvm.oom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
 * 当实例化 5 个 HeapOOM 对象后就OOM了
 *
 * @author kuangcp on 4/3/19-10:11 PM
 */
@Slf4j
public class HeapOOM {

  void createArray() {
    List<byte[]> data = new ArrayList<>();
    while (true) {
      data.add(new byte[1024 * 1024]);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Key {

    Integer id;

    Key(Integer id) {
      this.id = id;
    }

    // 重写了hashCode 没有重写 equals 导致了该对象在Map这样的集合中作Key时, 不能按预期的覆盖旧值而是共存
    @Override
    public int hashCode() {
      return id.hashCode();
    }
  }

  void createMap() {
    Map<Key, String> m = new HashMap<>();
    while (true) {
      for (int i = 0; i < 10000; i++) {
        if (!m.containsKey(new Key(i))) {
          m.put(new Key(i), "Number:" + i);
        }
      }
      log.info("m.size()=" + m.size());
    }
  }
}
