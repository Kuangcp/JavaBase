package syntax.string;

import org.junit.Test;
import syntax.bit.BitOperatorsTest;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Kuangcp
 * 2024-04-28 22:02
 */
public class RegexTest {

    // 缓存性能是未缓存的 3-5 倍
    @Test
    public void testCompareCache() throws Exception {
        for (int i = 0; i < 100; i++) {
            compare(10_0000);
        }
    }

    @Test
    public void testJoinPoolOpt() throws Exception {
        for (int i = 0; i < 300; i++) {
            compareByPool(100_0000);
        }
    }

    private static void compareByPool(int scale) {
        long start = System.currentTimeMillis();

        IntStream.range(0, scale).parallel().forEach(v -> {
            Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");
            pt.matcher("category");
            pt.matcher("category(who)（name）");
        });

        long end = System.currentTimeMillis();
        long no = end - start;

        start = System.currentTimeMillis();
        Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");

        IntStream.range(0, scale).parallel().forEach(v -> {
            pt.matcher("category");
            pt.matcher("category(who)（name）");
        });
        end = System.currentTimeMillis();
        long cac = end - start;

        start = System.currentTimeMillis();

        IntStream.range(0, scale).parallel().forEach(v -> {
            hasBracket("category");
            hasBracket("category(who)（name）");
        });
        end = System.currentTimeMillis();
        long loop = end - start;
        System.out.println(no + " " + cac + " " + loop);
    }

    /**
     * https://www.jianshu.com/p/f313746119ad
     */
    private static void compare(int scale) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < scale; i++) {
            Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");
            pt.matcher("category");
            pt.matcher("category(who)（name）");
        }
        long end = System.currentTimeMillis();
        long no = end - start;

        start = System.currentTimeMillis();
        Pattern pt = Pattern.compile("（.*?）|\\(.*?\\)");
        for (int i = 0; i < scale; i++) {
            pt.matcher("category");
            pt.matcher("category(who)（name）");
        }
        end = System.currentTimeMillis();
        long cac = end - start;
        System.out.println(no + " " + cac);
    }

    /**
     * TODO 比缓存的正则慢
     *
     * @see BitOperatorsTest
     */
    private static boolean hasBracket(String msg) {
        if (Objects.isNull(msg)) {
            return false;
        }
        int res = 0b0;
        for (int i = 0; i < msg.length(); i++) {
            if (Objects.equals(msg.charAt(i), '（')) {
                res = res | 0b1;
            } else if (Objects.equals(msg.charAt(i), '）')) {
                res = res | 0b10;
            } else if (Objects.equals(msg.charAt(i), '(')) {
                res = res | 0b100;
            } else if (Objects.equals(msg.charAt(i), ')')) {
                res = res | 0b1000;
            }
        }
//        int a = res & 0b11;
//        int b = res & 0b1100;
//        System.out.println(a + " " + b);
//        System.out.println(a == 0b11);
//        System.out.println(b == 0b1100);
        return (res & 0b11) == 0b11 || (res & 0b1100) == 0b1100;
    }

    @Test
    public void testBracket() throws Exception {
        System.out.println(hasBracket("a(sss）"));
        System.out.println(hasBracket("a(sss)"));
        System.out.println(hasBracket("a（sss）"));
        System.out.println(hasBracket("a(sss（"));
    }
}
