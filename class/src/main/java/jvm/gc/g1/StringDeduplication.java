package jvm.gc.g1;

import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-05-16 23:36
 *
 * -Xms15m -Xmx15m -XX:+PrintGCDetails -XX:+UseG1GC -XX:+PrintStringTableStatistics -XX:+UseStringDeduplication -XX:+PrintStringDeduplicationStatistics
 */
public class StringDeduplication {

  // TODO 如何设计用例 体现去重特性
  public static void main(String[] args) throws InterruptedException {
    for (int j = 0; j < 100; j++) {
      for (int i = 0; i < 1000; i++) {
        String.valueOf(i).intern();
      }
      TimeUnit.MILLISECONDS.sleep(30);
    }
  }
}
