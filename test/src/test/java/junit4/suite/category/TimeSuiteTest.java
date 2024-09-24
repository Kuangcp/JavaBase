package junit4.suite.category;

import junit4.suite.PartATest;
import junit4.suite.PartBTest;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Kuangcp
 * 2024-09-24 15:01
 */
@RunWith(Categories.class)
@Suite.SuiteClasses({
        PartATest.class,
        PartBTest.class,
})
//@Categories.ExcludeCategory(TimeTestCategory.class) // 排除标记的分类
@Categories.IncludeCategory(TimeTestCategory.class) // 只执行标记的分类
public class TimeSuiteTest {
}
