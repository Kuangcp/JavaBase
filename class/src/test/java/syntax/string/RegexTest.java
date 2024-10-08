package syntax.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import syntax.bit.BitOperatorsTest;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Kuangcp
 * 2024-04-28 22:02
 */
@Slf4j
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

    final Pattern back = Pattern.compile("^(,?\\d+)+$");
    final Pattern opt = Pattern.compile("^\\d+(,\\d+)*$");

    private boolean isNumberBacktracking(String str) {
        return back.matcher(str).find();
    }

    private boolean isNumber(String str) {
        return opt.matcher(str).find();
    }

    @Test
    public void testIsNumberWithThousand() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            isNumberBacktracking("123,456,789,654,111,123,456,789,654,111,111p");
        }
        long end = System.currentTimeMillis();
        log.info("{}", end - start);


        start = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            isNumber("123,456,789,654,111,123,456,789,654,111,111p");
        }
        end = System.currentTimeMillis();
        log.info("{}", end - start);
    }

    @Test
    public void testPossessive() throws Exception {
        final Pattern opt = Pattern.compile("ab{1,3}+c");
        System.out.println(opt.matcher("abc").find());
        System.out.println(opt.matcher("abbc").find());
        System.out.println(opt.matcher("abbbc").find());
        System.out.println(opt.matcher("abbbbc").find());
    }

    @Test
    public void testGroup() throws Exception {
        Pattern idxGroup = Pattern.compile("(\\d{4})-(\\d{2})");
        Matcher matcher = idxGroup.matcher("2012-12");
        System.out.println(matcher.matches());
        assertThat(matcher.group(1), equalTo("2012"));
        assertThat(matcher.group(2), equalTo("12"));

        Pattern nameGroup = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})");
        matcher = nameGroup.matcher("2012-12");
        System.out.println(matcher.matches());
        assertThat(matcher.group("year"), equalTo("2012"));
        assertThat(matcher.group("month"), equalTo("12"));
    }
}
