package base.string;

import com.github.kuangcp.time.GetRunTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-21  下午2:44
 */
public class SplitDemoTest {
    SplitDemo splitDemo = new SplitDemo();

    // 对比去除最后一个字符的性能, 第二个略好一些
    @Test
    public void testRemoveChar() throws Exception {
        GetRunTime time = GetRunTime.INSTANCE;
        time.startCount();
        for (int i = 0; i < 10000000; i++) {
            splitDemo.removeChar("122113.11.1.1.1.", ".");
        }
        time.endCount("索引去除");
        time.startCount();
        for (int i = 0; i < 10000000; i++) {
            splitDemo.removeLast("122113.11.1.1.1.");
        }
        time.endCount("去除最后一个字符");

    }

}