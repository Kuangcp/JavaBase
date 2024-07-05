package syntax.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class StringTest {

    /**
     * 字符串常量池 和 堆
     */
    @Test
    public void testConstantPoolAndHeap() {
        assert new String("1") != new String("1");

        assert "1" != new String("1");

        // 左边会被编译器替换为 "11"
        assert "1" + "1" != new String("1") + new String("1");
        assert "11" != new String("1") + new String("1");
        assert ("1" + "1").hashCode() == (new String("1") + new String("1")).hashCode();

        assert new String("1") + new String("1") != new String("1") + new String("1");

        assert "1" == "1";
    }

    /**
     * https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#intern--
     */
    @Test
    public void testIntern() {
        // 如果在 1.6上运行第一个断言就会通不过 intern() 会把首次遇到的字符串复制到常量池中, 并返回常量池中该字符串的引用

        // 在 1.7及以上: 不会复制实例，只是在常量池中记录首次出现的实例的引用,
        // 所以intern() 返回的引用和StringBuilder创建的实例是一样的
        String java = new StringBuilder("Ja").append("va").toString();
        assert java.intern() == java;

        // 这个断言通过是因为, Java字符串已经不是首次出现了, intern() 返回的是常量池里的引用, 和 StringBuilder实例不一致
        // https://www.zhihu.com/question/51102308 java字符串也是一致的结果
        String second = new StringBuilder("Ja").append("va").toString();
        Assert.assertNotSame(second.intern(), second);
    }

    @Test
    public void testConcat() throws Exception {
        String foo = "foo";
        // javap -v 查看编译器生成的字节码，能看到这里使用了 StringBuilder 进行替换
        String result = foo + "bar";
        //TODO complete
    }

    //TODO replace replaceAll replaceFirst

    @Test
    public void testReplaceSpecialChar() {
        "".replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
    }
}
