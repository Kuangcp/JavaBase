package base.string;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class SplitDemoTest {

  private SplitDemo splitDemo = new SplitDemo();

  // 对比去除最后一个字符的性能, 第二个略好一些
  @Test
  public void testRemoveChar() throws Exception {
    GET_RUN_TIME.startCount();
    for (int i = 0; i < 10000000; i++) {
      splitDemo.removeChar("122113.11.1.1.1.", ".");
    }
    GET_RUN_TIME.endCount("索引去除");
    GET_RUN_TIME.startCount();
    for (int i = 0; i < 10000000; i++) {
      splitDemo.removeLast("122113.11.1.1.1.");
    }
    GET_RUN_TIME.endCount("去除最后一个字符");

  }

}