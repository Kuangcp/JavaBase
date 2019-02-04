package base.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-9-12-下午7:05
 */
@Slf4j
public class LocalDateTest {

  @Test
  public void testFromLocalDateTime() {
    LocalDate localDate = LocalDateTime.now().toLocalDate();
    LocalDate last = LocalDateTime.now().plusDays(-1).toLocalDate();

    log.info("{}", localDate);
    log.info("{}", last);
  }
}
