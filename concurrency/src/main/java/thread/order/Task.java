package thread.order;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:53
 * 利用原子递增控制线程准入顺序。
 *
 * @author kuangcp
 */
@Slf4j
public class Task implements Runnable {

  private String target;
  private int order;
  private AtomicInteger count; //利用原子递增控制线程准入顺序。

  public Task(String target, int order, AtomicInteger count) {
    this.target = target;
    this.order = order;
    this.count = count;
  }

  @Override
  public void run() {
    while (true) {
      int count = this.count.get();
      if (count % 3 != order) {
        continue;
      }

      // 使用 log 会乱, 使用下面的逻辑也不能完全保障顺序
      this.count.incrementAndGet();
//      log.info("target={} order={} count={}", target, order, count);
      System.out.println(target + " " + order + " " + count);

      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
