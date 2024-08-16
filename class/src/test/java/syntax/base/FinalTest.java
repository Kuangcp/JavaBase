package syntax.base;

import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kuangcp
 * 2024-03-12 13:44
 */
public class FinalTest {


    public static final String x = "x" + System.getenv("");
    public static final String x2 = "x" + Optional.ofNullable(System.getenv("")).orElse("vvv");
    public static final String x3;
    public static final String x4;
    static Predicate<String> uu = v -> Objects.equals(v, "uu");

    // 只有这种情况会编译错误
    // TODO 为什么
//    private static final String x5 = "x" + Optional.ofNullable(System.getenv("")).filter(v -> Objects.equals(v, "uu")).orElse("vvv");

    private static final String x6 = "x" + Optional.ofNullable(System.getenv("")).filter(uu).orElse("vvv");

    static {
        x3 = "x" + Optional.ofNullable(System.getenv("")).filter(uu).orElse("vvv");
        x4 = "x" + Optional.ofNullable(System.getenv("")).filter(v -> Objects.equals(v, "uu")).orElse("vvv");
    }

    @Test
    public void testFinalInit() throws Exception {
        System.out.println(x);
        System.out.println(x2);
    }
}
