package base.time;

import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

/**
 * @author kuangcp on 18-7-26-下午4:45
 * apache common 工具包
 */
@Slf4j
public class OffsetTest {

  private Date lastSignInTime;

  public int signIn() {

    if(DateUtils.isSameDay(new Date(), lastSignInTime)){
      log.debug("uuuu: ={}");
      return 1;
    }

    Date yesterday = DateUtils.addDays(new Date(), -1);
    yesterday = DateUtils.setHours(yesterday, 0);
    yesterday = DateUtils.setMinutes(yesterday, 0);

    log.debug("compare: lastSignInTime={}, yesterday={}", lastSignInTime, yesterday);

    if (lastSignInTime.before(yesterday)) {
      System.out.println(1);
    } else {
      System.out.println("+1");
    }

    return 0;
  }

  @Test
  public void test() {
    Calendar instance = Calendar.getInstance();
    instance.set(2018, Calendar.JULY, 26, 1, 1);

    System.out.println("set " + instance.getTime());

    lastSignInTime = instance.getTime();
    signIn();

  }

}
