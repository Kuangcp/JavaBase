package base.string;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class StringTest {

  // Forced type conversion
  @Test
  public void testConvert() {
    List<Object> list = new ArrayList<>();
    list.add(1);
    Object name = list.get(0);
    log.debug("name: {}", name);
    log.debug("is null : {}", name == null);
    log.debug("is String : {}", name instanceof String);
    log.debug("is int : {}", name instanceof Integer);

    list.add(null);
    list.add(null);

    log.debug("list.size {}", list.size());

    if (list.get(0) instanceof String) {
      String nullToString = (String) list.get(0);
      log.debug("string: {}", nullToString);
    } else {
      log.debug("not String : {}", list.get(0));
    }
  }

  // TODO instanceof 会影响性能么?


  @Test
  public void testUUID() {
    String uuid = UUID.randomUUID().toString();
    log.debug("uuid: ={}", uuid);
  }

}
