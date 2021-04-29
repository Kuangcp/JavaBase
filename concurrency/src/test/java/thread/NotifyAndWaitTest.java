package thread;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
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

  /**
   * https://www.cnblogs.com/x_wukong/p/4009709.html
   */
  static class Printer implements Runnable {

    private final String name;
    private final Object prev;
    private final Object self;

    private Printer(String name, Object prev, Object self) {
      this.name = name;
      this.prev = prev;
      this.self = self;
    }

    @Override
    public void run() {
      int count = 10;
      while (count > 0) {
        synchronized (prev) {
          synchronized (self) {
            System.out.print(name);
            count--;

            self.notify();
          }
          try {
            prev.wait();
          } catch (InterruptedException e) {
            log.error("", e);
          }
        }
      }
    }
  }


  @Test
  public void testSortedPrint() {
    Object aLock = new Object();
    Object bLock = new Object();
    Object cLock = new Object();

    Printer pa = new Printer("A", cLock, aLock);
    Printer pb = new Printer("B", aLock, bLock);
    Printer pc = new Printer("C", bLock, cLock);

    new Thread(pa).start();
    new Thread(pb).start();
    new Thread(pc).start();
  }

  /**
   * TODO
   *
   * https://www.zhihu.com/question/439926072
   * https://blog.csdn.net/qq_35181209/article/details/77362297
   */
  static class SpuriousWakeup extends Object {

    private final List<String> list;

    public SpuriousWakeup() {
      list = Collections.synchronizedList(new LinkedList<>());
    }

    public String removeItem() throws InterruptedException {
      print("in removeItem() - entering");

      synchronized (list) {
        if (list.isEmpty()) {  //这里用if语句会发生危险
          print("in removeItem() - about to wait()");
          list.wait();
          print("in removeItem() - done with wait()");
        }

        //删除元素
        String item = list.remove(0);

        print("in removeItem() - leaving");
        return item;
      }
    }

    public void addItem(String item) {
      print("in addItem() - entering");
      synchronized (list) {
        //添加元素
        list.add(item);
        print("in addItem() - just added: '" + item + "'");

        //添加后，通知所有线程
        list.notifyAll();
        print("in addItem() - just notified");
      }
      print("in addItem() - leaving");
    }
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }

  @Test
  public void test() {
    final SpuriousWakeup en = new SpuriousWakeup();

    Runnable runA = () -> {
      try {
        String item = en.removeItem();
        print("in run() - returned: '" + item + "'");
      } catch (InterruptedException ix) {
        print("interrupted!");
      } catch (Exception x) {
        print("threw an Exception!!!\n" + x);
      }
    };

    Runnable runB = () -> en.addItem("Hello!");
    try {
      //启动第一个删除元素的线程
      Thread threadA1 = new Thread(runA, "threadA1");
      threadA1.start();

      Thread.sleep(500);

      //启动第二个删除元素的线程
      Thread threadA2 = new Thread(runA, "threadA2");
      threadA2.start();

      Thread.sleep(500);
      //启动增加元素的线程
      Thread threadB = new Thread(runB, "threadB");
      threadB.start();

      Thread.sleep(10000); // wait 10 seconds

      threadA1.interrupt();
      threadA2.interrupt();
    } catch (InterruptedException e) {
      log.error("", e);
    }
  }
}
