package timeServer;

import netty.timeServer.TimeClient;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-20  上午10:55
 */
public class TimeClientTest {
    TimeClient timeClient = new TimeClient();

    @Test
    public void testConnect() throws Exception {
        timeClient.connect(8080, "127.0.0.1");
    }
}