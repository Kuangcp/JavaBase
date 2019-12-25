package jvm.oom;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * set max thread:  echo 1000 > /proc/sys/kernel/threads-max
 *
 * OutOfMemoryError: unable to create new native thread
 *
 * @author https://github.com/kuangcp on 2019-12-25 20:05
 */
public class ThreadOOM {

  public static void main(String[] args) {

    while (true) {
      ThreadPoolExecutor pool = new ThreadPoolExecutor(16, 20, 2, TimeUnit.MINUTES,
          new LinkedBlockingDeque<>());

      pool.submit(() -> {
        int a = 1;
      });

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
