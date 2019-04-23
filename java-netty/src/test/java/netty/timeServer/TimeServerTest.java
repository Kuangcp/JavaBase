package netty.timeServer;


import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author kuangcp on 2019-04-23 10:58 AM
 */
@Slf4j
public class TimeServerTest {

  private TimeClient timeClient = new TimeClient();
  private TimeServer timeServer = new TimeServer();

  //  @Test
  @Test(threadPoolSize = 5, invocationCount = 20)
  public void testClient() throws Exception {
    timeClient.connectLocal(TimeServer.port);
  }

  @Test
  public void testServer() throws Exception {
    timeServer.start();
  }
}
