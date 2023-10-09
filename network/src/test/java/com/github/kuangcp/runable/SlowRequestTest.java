package com.github.kuangcp.runable;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-05-17 22:07
 */
@Slf4j
public class SlowRequestTest {

  @Test
  public void testSlowSingleConnection() throws Exception {
    int num = 200;
    CountDownLatch latch = new CountDownLatch(num);
    ExecutorService pool = Executors.newFixedThreadPool(10);
    for (int i = 0; i < num; i++) {
      pool.submit(() -> {
        latch.countDown();
        try {
          Optional<String> result = TuLingRobotTest.get("http://localhost:8080/ping");
          assert result.isPresent();
//          log.info(result.get());
        } catch (Exception e) {
          log.error("", e);
        }
      });
    }

    while (latch.getCount() != 0) {
      TimeUnit.SECONDS.sleep(10);
    }
  }

  // TODO Http connection pool
  @Test
  public void testSlowHttpPool() throws Exception {

  }
}
