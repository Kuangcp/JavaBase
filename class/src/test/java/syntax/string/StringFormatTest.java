package syntax.string;

import java.text.DecimalFormat;
import java.time.Duration;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-17 11:53
 */
public class StringFormatTest {

  @Test
  public void testDecimalFormat() {
    DecimalFormat decimalFormat = new DecimalFormat("##");
    System.out.println(Float.parseFloat("0.512") * 100);
    String format = decimalFormat.format(Float.parseFloat("0.14") * 100);
    System.out.println(format);
  }

  @Test
  public void testPlaceholderFormat() {
    long mills = 799239284;
    Duration duration = Duration.ofMillis(mills);

    System.out.println(Math.ceil(2435 / 1000.0) % 60);
    String result = String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60,
        (int)Math.ceil(mills / 1000.0) % 60);
    System.out.println(result);
  }
}
