package base.string;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class SplitDemoTest {


  // 截取第一次 target 出现之前的字符串
  public String removeChar(String origin, String target) {
    int indexOf = origin.indexOf(target);
    if (indexOf == -1) {
      return origin;
    }
    return origin.substring(0, indexOf);
  }

  // 去除最后一个字符
  public String removeLast(String target) {
    if (Objects.isNull(target) || target.isEmpty()) {
      return "";
    }
    return target.substring(0, target.length() - 1);
  }

  // 对比去除最后一个字符的性能, 第二个略好一些
  @Test
  public void testRemoveChar() {
    GET_RUN_TIME.startCount();
    String origin = "122113.11.1.1.1.";
    for (int i = 0; i < 10; i++) {
      origin = removeChar(origin, ".");
      log.info("temp: origin={}", origin);
    }
    GET_RUN_TIME.endCount("索引去除");

    GET_RUN_TIME.startCount();
    for (int i = 0; i < 100; i++) {
      removeLast("122113.11.1.1.1.");
    }
    GET_RUN_TIME.endCount("去除最后一个字符");
  }
}