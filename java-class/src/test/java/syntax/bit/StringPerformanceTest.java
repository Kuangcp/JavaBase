package syntax.bit;

import com.github.kuangcp.time.GetRunTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/11/19-10:21 AM
 */
@Slf4j
public class StringPerformanceTest {

  private int repeat = 10000;
  private String strData = "1100101010100111110101001011";
  private int length = strData.length();
  private int intData = 212499787;

  @Test
  public void testReadString() {
    GetRunTime getRunTime = new GetRunTime().startCount();
    for (int i = 0; i < repeat; i++) {
      readString();
    }
    getRunTime.endCount("string");
  }

  @Test
  public void testReadBit() {
    GetRunTime getRunTime = new GetRunTime().startCount();
    for (int i = 0; i < repeat; i++) {
      readBit();
    }
    getRunTime.endCount("bit");
  }

  private void readBit() {
    int temp = intData;
    for (int i = 0; i < length; i++) {
      if (temp % 2 == 1) {
        logic(true);
      } else {
        logic(false);
      }
      temp >>= 1;
    }
  }

  private void readString() {
    char[] chars = strData.toCharArray();
    for (int i = 0; i < length; i++) {
      char c = chars[i];
      if ('1' == c) {
        logic(true);
      } else if ('0' == c) {
        logic(false);
      }
    }
  }

  private void logic(boolean result) {
//    log.info("result {}", result);
  }
}
