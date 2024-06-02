package thread.waitnotify;

import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-06-02 17:22
 */
public class SortedPrintTest {

    @Test
    public void testSortedPrint() {
        Object aLock = new Object();
        Object bLock = new Object();
        Object cLock = new Object();

        SortedPrint pa = new SortedPrint("A", cLock, aLock);
        SortedPrint pb = new SortedPrint("B", aLock, bLock);
        SortedPrint pc = new SortedPrint("C", bLock, cLock);

        new Thread(pa).start();
        new Thread(pb).start();
        new Thread(pc).start();
    }
}
