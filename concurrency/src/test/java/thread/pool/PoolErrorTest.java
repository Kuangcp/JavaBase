package thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

/**
 * @author Kuangcp
 * 2024-08-16 10:59
 */
@Slf4j
public class PoolErrorTest {

    @Test
    public void testNoMethod() throws Exception {
        ExecutorsThreadPool.pool.execute(() -> {
            //TODO 3.1.1 3.6.1 版本差异 低版本没有该方法，报NoSuchMethodError, 但是日志中没有错误栈，导致问题难定位
            try {
                Pair<String, String> a = Pair.create("", "");
                log.info("finish");
            } catch (Throwable e) {
                log.error("", e);
            }
        });

        Thread.currentThread().join();
    }
}
