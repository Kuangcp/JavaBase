package jvm.oom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Java7以下 运行时字符串常量池是放在了永久代中, 就会报错 OOM: Perm space
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 *
 * Java7以上包括7, 运行时字符串常量池放在了堆中(Eden), 所以需要通过 -Xmx128M 来设限
 * 最终报错: OOM heap space 或者 GC overhead limit exceeded
 *
 * 同样的在Docker容器中运行, 同样的被Killed
 *
 * https://www.cnblogs.com/paddix/p/5309550.html
 *
 * @author kuangcp on 4/4/19-12:20 AM
 */
public class RunTimeConstantPoolOOM {

  public static void main(String[] args) throws InterruptedException {
    List<String> data = new ArrayList<>();
    int i = 0;
    Optional<String> result = IntStream.rangeClosed(1, 1000).mapToObj(String::valueOf)
        .reduce(String::concat);
    assert result.isPresent();

    while (true) {
      data.add((result.get() + i++).intern());
      // 如果加上睡眠, 就会发现 Eden 一直涨, 然后被回收掉, 并且部分转移到了 Old
      // 但是转移的内存大小大约为Eden的1/4? 慢慢的 最终 OOM

      // 如果不加, 瞬间 Old Gen 爆满然后 OOM heap space
//      TimeUnit.MILLISECONDS.sleep(1);
    }
  }
}
