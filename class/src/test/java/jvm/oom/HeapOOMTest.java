package jvm.oom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * -Xms30m -Xmx30m -XX:+PrintGCDetails
 * JDK9 以上 -Xlog:gc 更清晰
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
    public void testCreateArrayRecovery() throws Exception {
        heapOOM.createArrayRecovery();
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