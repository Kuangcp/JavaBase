package junit4.suite;

import junit4.suite.category.TimeTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author Kuangcp
 * 2024-09-24 14:12
 */
public class PartATest {

    @Test
    public void testA() throws Exception {
        System.out.println("A");
    }

    @Category(TimeTestCategory.class)
    @Test
    public void testTime() throws Exception {
        System.out.println("A time");
    }
}
