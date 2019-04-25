package thread;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-19 9:07 PM
 */
@Slf4j
public class ThreadInterruptedDemo {

  public static void main(String[] args) throws InterruptedException {
    Thread sleep = new Thread(new SleepRunner(), "Sleep");
    sleep.setDaemon(true);

    Thread busy = new Thread(new BusyRunner(), "Busy");
    busy.setDaemon(true);
    sleep.start();
    busy.start();

    TimeUnit.SECONDS.sleep(5);
    sleep.interrupt();
    busy.interrupt();
    log.info("sleep={}", sleep.isInterrupted());
    log.info("busy={}", busy.isInterrupted());

    // sleep 方法收到中断信号后， 会抛出中断异常并返回 false 所以这里的输出 是 false true
  }

  static class SleepRunner implements Runnable {

    @Override
    public void run() {
      while (true) {
        try {
          TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  static class BusyRunner implements Runnable {

    @Override
    public void run() {
      while (true) {

      }
    }
  }
}
