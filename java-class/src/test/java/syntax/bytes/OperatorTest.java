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
  public void testToInt() {
    int a = 13232;
    // 直接赋值 无法编译通过
    byte b = (byte)a;

    // int 赋值 byte 会截断后8位, 由于是有符号的, 结果是 10110000
    // 原码则是 01010000 : 所以 b是-80
    log.info("{}: {} -> {}", b, ShowBinary.toBinary(a), ShowBinary.toBinary(b));
  }

  @Test
  public void testMod(){
    byte a = -64;
    byte b = -4;
    // TODO 取余操作
    log.info("{}", a%b);
  }
}
