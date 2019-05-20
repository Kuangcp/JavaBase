package jvm.oom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Java7以下 运行时字符串常量池是放在了永久代中, 就会报错 OOM: Perm space
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 *
 * Java7以上包括7, 运行时字符串常量池放在了堆中(Eden), 所以需要通过设置堆大小来限制 -Xms5m -Xmx5m
 * 最终报错: OOM heap space 或者 GC overhead limit exceeded
 *
 * https://www.cnblogs.com/paddix/p/5309550.html
 *
 * 字符串的常量池不会被GC, 即使没有任何对象去引用
 *
 * @author kuangcp on 4/4/19-12:20 AM
 */
public class RunTimeConstantPoolOOM {
  private static List<String> data = new ArrayList<>();

  public static void main(String[] args) throws InterruptedException {
    int i = 0;
    // 先生成一个长字符串
    Optional<String> result = IntStream.rangeClosed(1, 1000).mapToObj(String::valueOf)
        .reduce(String::concat);
    assert result.isPresent();

    while (true) {
      // 只要出现了显式的 + 字符串拼接, 就会创建新的字符串, intern() 方法调用与否结果都是一样的
//      data.add((result.get() + i++).intern());
      data.add((result.get() + i++));

      // 如果不加, 瞬间 Old Gen 爆满然后 OOM : heap space
      // 如果加上, 就会发现 Eden 一直涨, 然后被回收掉, 并且部分转移到了 Old
      // 还能通过 visualvm 查看 内存的慢慢涨上去, 直到满了后 OOM, 内存被一下子释放掉
      TimeUnit.MILLISECONDS.sleep(20);
    }
  }
}
