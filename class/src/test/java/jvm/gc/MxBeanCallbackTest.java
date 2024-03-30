package jvm.gc;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-30 10:50
 */
public class MxBeanCallbackTest {

    @Test
    public void testInstallGCMonitoring() throws Exception {

        MxBeanCallback.installGCMonitoring();

        System.out.println("xx");
        TimeUnit.MINUTES.sleep(3);

    }
}
