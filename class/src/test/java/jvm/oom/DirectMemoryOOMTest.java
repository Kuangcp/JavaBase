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

    /**
     * 1. -XX:MaxDirectMemorySize=100m 分配到100M就OOM
     * 2. -Xmx260m分配到250M OOM 计算方式： xmx-Survivor
     */
    @Test
    public void testBuffer() throws InterruptedException {
        DirectMemoryOOM.byBuffer();
    }
}