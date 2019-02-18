package timeServer;

import netty.timeServer.TimeServer;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-20  上午10:53
 */
public class TimeServerTest {
    private TimeServer timeServer = new TimeServer();

    @Test
    public void testBind() throws Exception {
        timeServer.bind(8080);
    }
}