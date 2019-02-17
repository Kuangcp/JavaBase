package log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 * find correct log way by use logback
 *
 * @author kuangcp
 */
@Slf4j
public class CorrectLog {

  /**
   * F G H L is correct
   * result: L is recommend; G is improvise;
   *
   * G : log.error("", e); F G H actually is equivalent
   * L : log.error("{}", "Error : ", e);
   */
  @Test
  public void testCorrectLog() {
    try {
      double num = 1 / 0;
      System.out.println(num);
    } catch (Exception e) {
      // compile error
//      log.error(e); //A
//      log.error(e, e); //B

      // Exception ClassName and message
      log.debug("C");
      log.error("" + e); //C
      log.debug("D");
      log.error(e.toString()); //D

      // just message
      log.debug("E");
      log.error(e.getMessage()); //E

      log.debug("F");
      log.error(null, e); //F

      log.debug("G");
      log.error("", e); //G

      log.debug("H");
      log.error("{}", e); //H

      // just message
      log.debug("I");
      log.error("{}", e.getMessage()); //I

      // Exception ClassName and message
      log.debug("J");
      log.error("Error : " + e); //J

      // just message
      log.debug("K");
      log.error("Error : " + e.getMessage()); //K

      log.debug("L");
      log.error("{}", "Error : ", e); //L
    }
  }
}
