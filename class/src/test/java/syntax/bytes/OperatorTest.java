package syntax.bytes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.github.kuangcp.util.ShowBinary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-24 11:39 PM
 */
@Slf4j
public class OperatorTest {

    @Test
    public void testAdd() {
        byte a = 4;
        byte b = 2;

        int result = a / b;

        assertThat(result, equalTo(2));
        log.info("{} {} {}", result, ShowBinary.toBinary(a), ShowBinary.toBinary(b));
    }

    @Test
    public void testIntToByte() {
        int a = 13232;
        // 直接赋值 无法编译通过 需强转
        byte b = (byte) a;

        // int 赋值给 byte 仅保留后8位, 结果是 10110000
        // 反码 10101111
        // 原码 11010000
        // 所以 b是-80
        log.info("{} int: {} -> byte: {}", b, ShowBinary.toBinary(a), ShowBinary.toBinary(b));
    }

    // 和 int 一样的计算方式
    @Test
    public void testMod() {
        byte a = -64;
        byte b = -4;

        // 取余操作 a % b => a - (a / b) * b
        log.info("a % b = {} ", a % b);
        log.info("a / b = {}", a / b);
        log.info("(a / b) * b = {}", (a / b) * b);
        log.info("a - (a / b) * b = {}", a - (a / b) * b);
    }

    @Test
    public void testToString() {
        byte[] bytes = "you".getBytes();

        log.info("you={}", bytes);
        // 默认采用 UTF8 编码
        String result = new String(bytes);
        log.info("result={}", result);

        assertThat(result, equalTo("you"));
    }
}