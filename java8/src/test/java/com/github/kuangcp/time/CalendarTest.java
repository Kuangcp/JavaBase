package com.github.kuangcp.time;

import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Calendar 对象
 *
 * @author kuangcp on 18-8-4-下午10:16
 */
@Slf4j
public class CalendarTest {

  @Test
  public void testInit() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 20);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);

    log.debug("date: calendar={}", cal);
  }
}
