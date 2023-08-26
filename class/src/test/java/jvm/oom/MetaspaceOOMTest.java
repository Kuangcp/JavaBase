package jvm.oom;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2023-08-26 14:36
 */
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
}