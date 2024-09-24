package junit4.rule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runners.model.TestTimedOutException;

import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-09-24 14:37
 */
public class TimeoutTest {

    @Rule
    public Timeout timeout = new Timeout(100);

    /**
     * 注意 except 不生效
     */
    @Test(expected = TestTimedOutException.class)
    public void testRun() throws Exception {
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void testAdd() throws Exception {
        Assert.assertEquals(3, 1 + 2);
    }

    @Test
    public void testSin() throws Exception {
        double c = 0;
        for (int i = 0; i < 100000; i++) {
            double val = Math.sin(i) * Math.tan(i);
            c = Math.sin(val + c);
        }
    }
}
