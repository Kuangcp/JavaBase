package math;

import static org.junit.Assert.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-1-上午11:27
 * 概率是在宏观尺度上的准确性定理
 */
@Slf4j
@Ignore
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
    assertEquals(1000, count, 50);
    log.debug("total: count={}", count);
  }

  @Test
  public void testRandom2(){
    for (int i = 0; i < 10000; i++) {
      testRandom();
    }
  }
}
