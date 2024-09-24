package junit4.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Kuangcp
 * 2024-09-24 14:12
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // 注意此处的类顺序 就是单元测试方法的执行顺序
        PartATest.class,
        PartBTest.class,
})
public class SuiteTest {
}
