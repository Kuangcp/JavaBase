package syntax.integer;

import org.junit.Test;

import java.util.Objects;

/**
 * @author kuangcp on 4/2/19-12:50 PM
 */
public class IntegerCacheTest {

    @Test
    public void testCache() {
        // 缓存仅在 valueOf 方法中生效, 自动拆装箱又是调用的该方法, 所以缓存也存在于自动拆装箱里
        assert Integer.valueOf(1) == Integer.valueOf(1);
        assert Integer.valueOf(128) != Integer.valueOf(128);

        assert new Integer(1) != Integer.valueOf(1);
        assert new Integer(1) != new Integer(1);
    }

    @Test
    public void testInappropriateWithBox() {
        Integer sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
            // 编译后等价于 sum = Integer.valueOf((int)(sum.intValue() + i));
        }
        System.out.println(sum);
    }

    // 自动拆装箱 NPE问题
    @Test(expected = NullPointerException.class)
    public void testBoxWithNPE() {
        Integer num = null;
        if (num == 1) {
            System.out.println();
        }
    }

    //java中的基本类型的包装类、其中 Byte、Boolean、Short、Character、Integer、Long 实现了常量池技术
    // （除了Boolean，都只对小于128的值才支持）
    @Test
    public void testInteger() {
//        对于-128~127的Integer对象才会到IntegerCache里获取缓存，使用常量池技术
        Integer i1 = 100;
        Integer i2 = 100;
        // 上面两行代码，使用自动装箱特性，编译成
        // Integer i1 = Integer.valueOf(100);
        // Integer i2 = Integer.valueOf(100);
        System.out.println(i1 == i2);

        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println(i3 == i4);
    }

    @Test
    public void testAvoidBoxWithNPE() {
        Integer num = null;
        if (Objects.equals(num, 1)) {
            System.out.println("true");
        }
    }
}
