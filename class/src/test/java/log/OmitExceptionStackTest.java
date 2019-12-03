package log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://www.oracle.com/technetwork/java/javase/relnotes-139183.html
 * https://blog.csdn.net/zheng0518/article/details/52450537
 *
 * @author https://github.com/kuangcp on 2019-12-03 09:41
 */
@Slf4j
public class OmitExceptionStackTest {

  private void handleA() {
    String n = null;
    n.length();
  }

  private void handleB() {
    try {
      handleA();
    } catch (Exception e) {
      log.error("", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Test
  public void testNPE() {
    for (int i = 0; i < 50000; i++) {
      try {
        handleB();
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }
}
