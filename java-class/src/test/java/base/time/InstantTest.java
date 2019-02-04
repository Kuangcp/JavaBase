package base.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Instant 表示的是时刻(表示这个瞬间的时间点) 和时区无关
 * System.currentTimeMillis也是和时区无关的UTC时间
 *
 * @author kuangcp on 18-9-11-下午4:31
 */
@Slf4j
public class InstantTest {

  @Test
  public void testCurrentTimeMillis() {
    long epochMilli = Instant.now().toEpochMilli();
    long now = System.currentTimeMillis();

    assertThat(epochMilli, equalTo(now));
  }

}
