package base.list;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
  @Test(expected = NullPointerException.class)
  public void testNullLoop() {
    List<String> list = new ArrayList<>();
    List<Integer> intList = list.stream().map(Integer::parseInt).collect(Collectors.toList());

    log.info(": intList={}", intList);
    list.add("1");
    list.add("2");
    list.forEach(item -> log.debug("item : {}", item));
    list = null;

    log.debug("lambda foreach");
    list.forEach(item -> log.debug("item : {}", item));

    log.debug("NPE after foreach");
    for (String s : list) {
      log.debug("item : {}", s);
    }
  }

}
