package thread;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-05-09 11:01
 */
@Slf4j
public class NotifyAndWaitTest {

  private final Set<NotifyAndWait> set = new HashSet<>();

  @Test
  public void testLoop() throws InterruptedException {
    List<String> locks = IntStream.range(1, 6).mapToObj(String::valueOf)
        .collect(Collectors.toList());
    for (int i = 0; i < 500; i++) {
      NotifyAndWait notifyAndWait = new NotifyAndWait(locks.get(i % 5));
      set.add(notifyAndWait);
    }

    for (NotifyAndWait ele : set) {
      new Thread(() -> {
        try {
          Optional<String> result = ele.call();
          log.info("result={}", result);
        } catch (InterruptedException e) {
          log.error("", e);
        }
      }).start();
    }

    for (NotifyAndWait ele : set) {
      new Thread(() -> {
        try {
          Thread.sleep(4000);
          ele.back(System.currentTimeMillis() + "");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }

    Thread.sleep(300000);
  }

  @Test
  public void testMultiLoop() throws InterruptedException {
    ExecutorService pool = Executors.newFixedThreadPool(3);
    List<String> locks = IntStream.range(1, 6).mapToObj(String::valueOf)
        .collect(Collectors.toList());
    for (int i = 0; i < 500; i++) {
      NotifyAndWait notifyAndWait = new NotifyAndWait(locks.get(i % 5));
      set.add(notifyAndWait);
    }

    for (NotifyAndWait ele : set) {
      pool.submit(() -> {
        try {
          Optional<String> result = ele.call();
          log.info(": result={}", result);
        } catch (InterruptedException e) {
          log.error("", e);
        }
      });
    }


    for (NotifyAndWait ele : set) {
      pool.submit(() -> {
        try {
          Thread.sleep(500);
          ele.back(System.currentTimeMillis() + "");
        } catch (InterruptedException e) {
          log.error("", e);
        }
      });
    }

    Thread.sleep(300000);
  }
}
