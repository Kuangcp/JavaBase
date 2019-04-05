package jvm.oom;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author kuangcp on 4/5/19-9:45 AM
 */
public class HeapOOMTest {

  private HeapOOM heapOOM = new HeapOOM();

  @Test
  @Ignore
  public void testCreateArray() throws Exception {
    heapOOM.createArray();
  }

  @Test
  @Ignore
  public void testCreateMap() throws Exception {
    heapOOM.createMap();
  }
}