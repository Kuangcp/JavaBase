package netty.timeServer;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp on 18-3-20  上午10:53
 */
public class TimeServerTest {

  private TimeServer timeServer = new TimeServer();

  @Test
  public void testServer() throws Exception {
    timeServer.bind(8080);
  }
}