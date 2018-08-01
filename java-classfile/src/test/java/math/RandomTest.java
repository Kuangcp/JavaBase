package math;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-1-上午11:27
 */
@Slf4j
public class RandomTest {

  @Test
  public void testRandom() {
    int count = 0;
    for (int i = 0; i < 10000; i++) {

      double v = Math.random() * 1000;
      if (v > 900) {
        count++;
      }
    }
    log.debug("total: count={}", count);

  }
}
