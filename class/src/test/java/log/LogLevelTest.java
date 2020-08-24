package log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-07-11 17:01
 */
@Slf4j
public class LogLevelTest {

  private String invoke() {
    System.out.println("invoke");
    return "";
  }

  @Test
  public void testDebug() throws Exception {
    System.out.println("start");
    log.info("start");
    if (log.isDebugEnabled()) {
      log.debug(invoke());
    }
  }

  @Test
  public void testI() throws Exception {
    int i = 1;
    i = ++i + ++i;
    System.out.println(i);
  }
}
