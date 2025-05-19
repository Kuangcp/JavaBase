package jvm.oom;

import org.junit.Test;

/**
 * @author Kuangcp
 * 2025-05-19 21:36
 */
public class DirectMemoryGlibcGrowTest {

    @Test
    public void testFragment() throws Exception {
        DirectMemoryGlibcGrow.fragment();
        Thread.currentThread().join();
    }

    /**
     * pmap -x 259296 | jvm-tool -s 查看块数量
     */
    @Test
    public void testLoopFragment() throws Exception {
        DirectMemoryGlibcGrow.loopFragment();
        Thread.currentThread().join();
    }
}
