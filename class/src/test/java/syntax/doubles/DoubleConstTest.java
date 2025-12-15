package syntax.doubles;

import com.github.kuangcp.util.ShowBinary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/14/19-2:20 PM
 * TODO 浮点数 实现原理  Double 实现原理
 */
@Slf4j
public class DoubleConstTest {

    @Test
    public void testExtremum() {
        log.info("{} {}", ShowBinary.toBinary(Double.MIN_VALUE), ShowBinary.toBinary(Double.MIN_NORMAL));
    }

    @Test
    public void testCompare() throws Exception {
        System.out.println(0.1 + 0.2 == 0.3);
    }

    @Test
    public void testIntPerformance() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int j = 0; j < 10; j++) {
            int capacity = 32;
            int total = 0;
            long s1 = System.currentTimeMillis();
            int full = 2000000000;
            for (int i = 0; i < full + j; i++) {
                int re = (int) (capacity * 0.7191726);
                total += re;
            }
//        System.out.println(total);
            long e1 = System.currentTimeMillis();
            buf.append(total);
            System.out.println("-- " + (e1 - s1));

            total = 0;
            long s2 = System.currentTimeMillis();
            for (int i = 0; i < full + j; i++) {
                int re = (int) (capacity * 0.75);
                total += re;
            }
            long e2 = System.currentTimeMillis();
            buf.append(total);
            System.out.println((e2 - s2)+"--");
//        System.out.println(total);
        }
        System.out.println(buf.substring(0, 20));
    }
}
