package thread;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import thread.waitnotify.NotifyAndWait;
import thread.waitnotify.SortedPrint;
import thread.waitnotify.SpuriousWakeup;

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
          log.error("", e);
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

  @Test
  public void testSortedPrint() {
    Object aLock = new Object();
    Object bLock = new Object();
    Object cLock = new Object();

    SortedPrint pa = new SortedPrint("A", cLock, aLock);
    SortedPrint pb = new SortedPrint("B", aLock, bLock);
    SortedPrint pc = new SortedPrint("C", bLock, cLock);

    new Thread(pa).start();
    new Thread(pb).start();
    new Thread(pc).start();
  }

  private void testLogic(Supplier<String> rmFunc, Runnable addFunc) throws InterruptedException {
    Runnable remove = () -> {
      String item = rmFunc.get();
      log.info("in run() - returned: '" + item + "'");
    };

    //启动第一个删除元素的线程
    Thread threadA1 = new Thread(remove, "threadA1");
    threadA1.start();
    Thread.sleep(500);

    //启动第二个删除元素的线程
    Thread threadA2 = new Thread(remove, "threadA2");
    threadA2.start();
    Thread.sleep(500);

    System.out.println();

    //启动增加元素的线程
    Thread threadB = new Thread(addFunc, "threadB");
    threadB.start();
    Thread.sleep(4000);
    log.info("complete sleep");
  }

  @Test
  public void test() throws InterruptedException {
    final SpuriousWakeup en = new SpuriousWakeup();
    testLogic(en::removeItem, () -> en.addItem("Hello!"));
  }

  @Test
  public void testWithWhile() throws InterruptedException {
    final SpuriousWakeup en = new SpuriousWakeup();
    testLogic(en::removeItemWithWhile, () -> en.addItem("Hello!"));
  }
}
