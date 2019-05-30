package syntax.tryblock;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * finally block should not contain return, the ide warn you as same
 * execution sequence: expression in return then cache in stack, execution finally, return(the return on finally cover the try)
 *
 * @author kuangcp on 2/17/19-9:24 AM
 */
@Slf4j
public class FinallyWithReturn {

  @Test
  public void testFinallyWithReturn() {
    int value = doSomethingWithCoverException();
    log.info("actual value: value={}", value);
  }

  @Test
  public void testFinallyWithoutReturn() {
    int value = doSomething();
    log.info("actual value: value={}", value);
  }

  // 1. finally block will override try block return value
  // 2. may cover Exception, if not catch corresponding exception
  private int doSomethingWithCoverException() {
    try {
      return alwaysZero();
    } finally {
      log.info("finally block");
      // if use return, even the compile-time exception will ignored
      // this return will cover the return on try block
      return 1;
    }
  }

  // correct way
  private int doSomething() {
    try {
      return alwaysZero();
    } catch (IOException e) {
      log.warn("catch exception ", e);
    } finally {
      log.info("finally block");
    }
    return 1;
  }

  private int alwaysZero() throws IOException {
    log.info("invoke alwaysZero method");
    if (ThreadLocalRandom.current().nextBoolean()) {
      log.warn("throw Exception");
      throw new IOException("io exception");
    }

    return 0;
  }
}
