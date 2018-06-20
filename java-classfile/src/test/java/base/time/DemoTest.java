package base.time;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class DemoTest {

  @Test
  public void testInit() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    System.out.println(timestamp.toString());

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, timestamp.toLocalDateTime().getDayOfMonth() + 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Timestamp end = new Timestamp(cal.getTimeInMillis());
    System.out.println(end);
    System.out.println(timestamp.before(end));

  }

  @Test
  public void testLoop(){


  }

  @Test
  public void testInstant() {
    Instant instant = Instant.now();
    System.out.println(instant);
  }

  @Test
  public void testReset() {
    LocalDateTime receive = LocalDateTime.of(2018, 5, 10, 12, 22, 0);
    boolean result = isNeedReset(Timestamp.valueOf(receive), QuestConstants.STEP_MONTH);
    System.out.println(receive + " = " + result);

    receive = LocalDateTime.of(2018, 6, 10, 12, 22, 0);
    result = isNeedReset(Timestamp.valueOf(receive), QuestConstants.STEP_DAY);
    System.out.println(receive + " = " + result);
    receive = LocalDateTime.of(2018, 6, 30, 12, 22, 0);
    result = isNeedReset(Timestamp.valueOf(receive), QuestConstants.STEP_WEEK);
    System.out.println(receive + " = " + result);
    receive = LocalDateTime.of(2018, 6, 30, 12, 22, 0);
    result = isNeedReset(Timestamp.valueOf(receive), QuestConstants.STEP_DAY);
    System.out.println(receive + " = " + result);

  }

  private boolean isNeedReset(Timestamp receiveTime, int step) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, QuestConstants.RESET_TIME);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);

    switch (step) {
      case QuestConstants.STEP_DAY:
        cal.set(Calendar.DAY_OF_MONTH, receiveTime.toLocalDateTime().getDayOfMonth() + 1);
        break;
      case QuestConstants.STEP_WEEK:
        int difference = 7 - receiveTime.toLocalDateTime().getDayOfWeek().getValue()
            + QuestConstants.RESET_DAY_OF_WEEK;
        cal.set(Calendar.DAY_OF_MONTH, receiveTime.toLocalDateTime().getDayOfMonth() + difference);
        break;
      case QuestConstants.STEP_MONTH:
        cal.set(Calendar.DAY_OF_MONTH, QuestConstants.RESET_DAY_OF_MONTH);
        cal.set(Calendar.MONTH, receiveTime.toLocalDateTime().getMonth().getValue());
        break;
      default:
        return false;
    }
    Timestamp end = new Timestamp(cal.getTimeInMillis());
    System.out.println("任务重置时间 "+end);
    return Timestamp.valueOf(LocalDateTime.now()).after(end);
  }

  interface QuestConstants {
    // 任务类型
    int TYPE_MAIN = 1;
    int TYPE_DAILY = 2;
    int TYPE_TIME_LIMIT = 3;

    // 重置周期
    int STEP_NONE = 0;
    int STEP_FINISH = 1; // 领取奖励后才重置
    int STEP_DAY = 2;
    int STEP_WEEK = 3;
    int STEP_MONTH = 4;

    int RESET_TIME = 0; // 重置时间
    int RESET_DAY_OF_WEEK = 2;
    int RESET_DAY_OF_MONTH = 1;
  }
}
