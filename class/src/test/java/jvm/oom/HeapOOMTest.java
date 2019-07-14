package jvm.oom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * -Xms30m -Xmx30m -XX:+PrintGCDetails
 *
 * @author kuangcp on 4/5/19-9:45 AM
 */
@Ignore
@Slf4j
public class HeapOOMTest {

  private HeapOOM heapOOM = new HeapOOM();

  @Test
  public void testCreateArray() {
    heapOOM.createArray();
  }

  @Test
  public void testCreateMap() {
    heapOOM.createMap();
  }

  @Test
  public void testCreateWeakMap() {
    heapOOM.createWeakMap();
  }

  @Test
  public void testOtherThreadWithOOM() throws Exception {
    heapOOM.otherThreadWithOOM();
  }
}