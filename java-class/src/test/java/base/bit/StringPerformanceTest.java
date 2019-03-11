package base.bit;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import org.junit.Test;

/**
 * @author kuangcp on 3/11/19-10:21 AM
 */
public class StringPerformanceTest {

  private int length = 6;
  private int repeat = Integer.MAX_VALUE;
  private String strData = "1100101010100111110101001011";
  private int intData = 212499787;

  @Test
  public void testReadString() {
    GET_RUN_TIME.startCount();
    for (int i = 0; i < repeat; i++) {
      readString();
    }
    GET_RUN_TIME.endCount("string");
  }

  @Test
  public void testReadBit() {
    GET_RUN_TIME.startCount();
    for (int i = 0; i < repeat; i++) {
      readBit();
    }
    GET_RUN_TIME.endCount("bit");
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
  }
}
