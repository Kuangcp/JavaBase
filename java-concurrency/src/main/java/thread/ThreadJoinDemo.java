package thread;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-22 9:55 AM
 */
@Slf4j
public class ThreadJoinDemo {

  static class Domino implements Runnable {

    private Thread thread;

    public Domino(Thread thread) {
      this.thread = thread;
    }

    @Override
    public void run() {
      try {
        thread.join();
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }

      log.info("{} terminate.", Thread.currentThread().getName());
    }

  }

  // 每个线程的需要等待前驱线程执行完成才能退出
  public static void main(String[] args) throws InterruptedException {
    Thread previous = Thread.currentThread();

    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(new Domino(previous), i + "");
      thread.start();
      previous = thread;

    }
    TimeUnit.SECONDS.sleep(3);
    log.info("{} terminate.", Thread.currentThread().getName());
  }
}
