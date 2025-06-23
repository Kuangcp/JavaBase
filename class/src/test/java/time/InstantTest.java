package time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
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

  @Test
  public void testCreateInstant() {
    // 从纪元时间创建
    Instant epoch = Instant.EPOCH; // 1970-01-01T00:00:00Z
    assertEquals(0, epoch.getEpochSecond());

    // 从秒创建
    Instant fromSeconds = Instant.ofEpochSecond(1000);
    assertEquals(1000, fromSeconds.getEpochSecond());

    // 从毫秒创建
    Instant fromMillis = Instant.ofEpochMilli(1000);
    assertEquals(1, fromMillis.getEpochSecond());

    // 解析字符串
    Instant parsed = Instant.parse("2023-01-01T00:00:00Z");
    assertTrue(parsed.toString().startsWith("2023"));
  }

  @Test
  public void testCompareInstants() {
    Instant now = Instant.now();
    Instant future = now.plusSeconds(10);
    Instant past = now.minusSeconds(10);

    assertTrue(now.isBefore(future));
    assertTrue(now.isAfter(past));
    assertTrue(now.equals(now));

    // 比较两个时间点的差异
    Duration duration = Duration.between(past, future);
    assertEquals(20, duration.getSeconds());
  }

  @Test
  public void testModifyInstant() {
    Instant base = Instant.parse("2023-01-01T00:00:00Z");
    
    // 增加时间
    Instant plus1Hour = base.plus(1, ChronoUnit.HOURS);
    assertEquals("2023-01-01T01:00:00Z", plus1Hour.toString());

    // 减少时间
    Instant minus30Mins = base.minus(30, ChronoUnit.MINUTES);
    assertEquals("2022-12-31T23:30:00Z", minus30Mins.toString());

    // 使用Duration进行修改
    Instant plusDuration = base.plus(Duration.ofDays(1));
    assertEquals("2023-01-02T00:00:00Z", plusDuration.toString());
  }

  @Test
  public void testTruncateInstant() {
    Instant now = Instant.parse("2023-01-01T12:34:56.789Z");
    
    // 截断到秒
    Instant truncatedToSeconds = now.truncatedTo(ChronoUnit.SECONDS);
    assertEquals("2023-01-01T12:34:56Z", truncatedToSeconds.toString());

    // 截断到分钟
    Instant truncatedToMinutes = now.truncatedTo(ChronoUnit.MINUTES);
    assertEquals("2023-01-01T12:34:00Z", truncatedToMinutes.toString());

    // 截断到小时
    Instant truncatedToHours = now.truncatedTo(ChronoUnit.HOURS);
    assertEquals("2023-01-01T12:00:00Z", truncatedToHours.toString());
  }
}
