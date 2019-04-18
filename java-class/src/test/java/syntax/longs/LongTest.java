package syntax.longs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-18 11:13 PM
 */
@Slf4j
public class LongTest {

  // 原因很简单, 前三个编译后实际上是一样的代码, 而这个转换都是走了Long的缓存 LongCache
  //    Long a = 3L;
  //    Long b = 3L;
  //    Long c = 3L;
  @Test
  public void testCache() {
    Long a = (long) 3;
    Long b = 3L;
    Long c = Long.valueOf(3);
    Long d = new Long(3);

    assert a == b;
    assert a == c;
    assert a != d;
    assert b == c;
    assert b != d;
    assert c != d;
  }
}
