package jvm.oom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2023-08-26 14:36
 */
@Slf4j
public class MetaspaceOOMTest {

    /**
     * 测试Arthas注入对元空间内存的占用情况
     * <p>
     * -XX:MaxMetaspaceSize=25m -Xmx500m
     * 步骤：
     * 1. 启动单元测试
     * 2. Arthas注入
     * 3. visualvm 注入
     */
    @Test
    public void testArthasInject() throws Exception {
        // 睡眠是为了运行 arthas 和 visualvm

        // arthas 注入后 20M，注入前是770K左右
        TimeUnit.SECONDS.sleep(20);

        // 开始反射和JSON序列化后，迅速OOM
        // 该方法执行需要15M左右
        MetaspaceOOM.jacksonAndReflect();

        // 如果是等业务方法执行完后注入，arthas会注入失败，提示OOM
        TimeUnit.HOURS.sleep(1);
    }

    /**
     * 通过 每3s 千万次周期运行 可以看到metaspace有缓慢增长的趋势，但是离引发故障还差很远，
     * 需要继续确认是否确实双括号实例化对象是否有匿名类泄漏问题
     */
    @Test
    public void testAnonymousClass() throws Exception {
        TimeUnit.SECONDS.sleep(20);
        log.info("start");
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(MetaspaceOOM::anonymousClass, 0, 3, TimeUnit.SECONDS);
//        MetaspaceOOM.anonymousClass();
//        log.info("end");

        TimeUnit.HOURS.sleep(1);
    }
}