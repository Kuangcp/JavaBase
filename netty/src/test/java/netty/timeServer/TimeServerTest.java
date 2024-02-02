package netty.timeServer;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-23 10:58 AM
 */
@Slf4j
public class TimeServerTest {

    private final TimeServer timeServer = new TimeServer();

    @Test
    public void testServer() throws Exception {
        timeServer.start();
    }
}
