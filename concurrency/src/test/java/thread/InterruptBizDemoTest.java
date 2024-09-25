package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Kuangcp
 * 2024-09-25 09:34
 */
@Slf4j
public class InterruptBizDemoTest {


    @Test
    public void testSingleThread() throws Exception {
        log.info("start sin");

        InterruptBizDemo.singleThreadMode();
    }

    @Test
    public void testConsumer() throws Exception {
        log.info("start");

        InterruptBizDemo.consumerScheduler();
    }
}
