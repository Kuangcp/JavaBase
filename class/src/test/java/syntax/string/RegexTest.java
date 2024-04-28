package syntax.string;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author Kuangcp
 * 2024-04-28 22:02
 */
public class RegexTest {

    // 缓存性能是未缓存的 3-5 倍
    @Test
    public void testCompareCache() throws Exception {
        for (int i = 0; i < 100; i++) {
            compare();
        }
    }

    private static void compare() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");
            pt.matcher("category");
            pt.matcher("category(who)（name）");
        }
        long end = System.currentTimeMillis();
        long no = end - start;

        start = System.currentTimeMillis();
        Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");
        for (int i = 0; i < 100000; i++) {
            pt.matcher("xxxx");
            pt.matcher("xxxx(who)");
        }
        end = System.currentTimeMillis();
        long cac = end - start;
        System.out.println(no + " " + cac);
    }
}
