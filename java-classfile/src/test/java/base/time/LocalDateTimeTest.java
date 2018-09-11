package base.time;


import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-9-11-下午3:52
 */
@Slf4j
public class LocalDateTimeTest {


  // LocalDateTime 实例的存在是和时区有关联的
  // System.currentTimeMillis 是采用的该系统的默认时区的时间 减去 UTC时间的 1970
  @Test
  public void testNow() {
    LocalDateTime now = LocalDateTime.now();
    long milli = now.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();

    log.info(": milli={}", milli);
    log.info(": milli={}", System.currentTimeMillis());

    assertThat(System.currentTimeMillis() - milli, lessThan(1000L));
  }
}
