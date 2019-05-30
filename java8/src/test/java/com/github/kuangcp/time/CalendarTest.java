package com.github.kuangcp.time;

import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 * Calendar 对象
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

//    java.util.GregorianCalendar[time=?,
// areFieldsSet=false,areAllFieldsSet=true,
// lenient=true,zone=sun.util.calendar.ZoneInfo[id="Asia/Shanghai",
// offset=28800000,dstSavings=0,useDaylight=false,transitions=19,
// lastRule=null],
// firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,
// YEAR=2018,MONTH=7,WEEK_OF_YEAR=31,WEEK_OF_MONTH=1,DAY_OF_MONTH=-16,
// DAY_OF_YEAR=216,DAY_OF_WEEK=7,DAY_OF_WEEK_IN_MONTH=1,AM_PM=1,
// HOUR=10,HOUR_OF_DAY=0,MINUTE=0,SECOND=0,MILLISECOND=0,
// ZONE_OFFSET=28800000,DST_OFFSET=0]

    log.debug("date: calendar={}", cal);
  }
}
