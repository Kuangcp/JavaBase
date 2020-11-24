package jvm.oom;

import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-11-24 14:45
 */
public class DirectMemoryOOMTest {

  @Test
  public void testUnsafe() throws InterruptedException, IllegalAccessException {
    DirectMemoryOOM.byUnsafe();
  }

  @Test
  public void testBuffer() throws InterruptedException {
    DirectMemoryOOM.byBuffer();
  }
}