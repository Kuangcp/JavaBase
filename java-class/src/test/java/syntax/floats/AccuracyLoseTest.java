package syntax.floats;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/1/19-3:28 PM
 * 问题的根源是 二进制无法表示所有十进制的数值
 */
@Slf4j
public class AccuracyLoseTest {

  // 陷入死循环
  @Test
  public void testLoop() throws InterruptedException {
    double sum;
    int count = 0;

    for (sum = 0; sum != 1; sum += 0.1) {
      count++;
      log.info("{}: {} \n", count, sum);
      Thread.sleep(200);
    }
  }
}
