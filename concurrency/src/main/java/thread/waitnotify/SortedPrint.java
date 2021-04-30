package thread.waitnotify;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.cnblogs.com/x_wukong/p/4009709.html
 */
@Slf4j
public class SortedPrint implements Runnable {

  private final String name;
  private final Object prev;
  private final Object self;

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

  public SortedPrint(String name, Object prev, Object self) {
    this.name = name;
    this.prev = prev;
    this.self = self;
  }
}