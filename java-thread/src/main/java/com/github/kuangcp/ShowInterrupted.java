package com.github.kuangcp;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-19 9:07 PM
 */
@Slf4j
public class ShowInterrupted {

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
