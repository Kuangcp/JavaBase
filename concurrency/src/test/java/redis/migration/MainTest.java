package redis.migration;

import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 2/6/19-1:34 PM
 */
public class MainTest {

  @Test
  public void testTransferAllKey() throws InterruptedException {
    RedisPoolProperty origin = new RedisPoolProperty();
    origin.setHost("127.0.0.1");
    origin.setPort(6667);
    origin.setTimeout(100);

    RedisPoolProperty target = new RedisPoolProperty();
    target.setHost("127.0.0.1");
    target.setPort(6667);
    target.setTimeout(100);

    Main main = new Main(origin, target, 4, 2);
    main.transferAllKey();
  }
}
