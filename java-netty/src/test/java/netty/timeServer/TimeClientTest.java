package netty.timeServer;


import org.testng.annotations.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class TimeClientTest {

  private TimeClient timeClient = new TimeClient();

  @Test(threadPoolSize = 20, invocationCount = 50)
//  @Test
  public void testClient() throws Exception {
    timeClient.connect(8080, "127.0.0.1");
  }
}