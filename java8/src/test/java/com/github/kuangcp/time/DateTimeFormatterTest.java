package com.github.kuangcp.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-11 20:41
 */
public class DateTimeFormatterTest {

  @Test
  public void testFormat(){
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String dateStr = formatter.format(now);
    System.out.println(dateStr);
  }
}
