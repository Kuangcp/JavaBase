package base.list;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 常用小知识点
 *
 * @author kuangcp on 18-7-12-下午12:27
 */
@Slf4j
public class UsuallyTest {

  /**
   * conclusion
   */
  @Test
  public void testNullLoop() {
    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.forEach(item -> {
      log.debug("item : {}", item);
    });
    list = null;

//    list.forEach(item ->{
//      log.debug("item : {}", item);
//    });
    for (String s : list) {
      log.debug("item : {}", s);
    }
  }

}
