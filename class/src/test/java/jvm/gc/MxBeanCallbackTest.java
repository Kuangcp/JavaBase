package jvm.gc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-30 10:50
 */
@Slf4j
public class MxBeanCallbackTest {

    /**
     * jcmd pid GC.run 触发GC，可以看到对应输出
     */
    @Test
    public void testInstallGCMonitoring() throws Exception {
        MxBeanCallback.installGCMonitoring();
        log.info("finish install");

        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        log.info("bean={}", bean);

        TimeUnit.MINUTES.sleep(3);
    }
}
