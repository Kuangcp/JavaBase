package syntax.bit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.github.kuangcp.time.GetRunTime;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/5/19-2:41 PM
 * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
 * >>> : https://stackoverflow.com/questions/14501233/unsigned-right-shift-operator-in-java
 *
 * 原码=>补码: 取反再加1 除符号位
 * 补码=>原码: 减1再取反 除符号位
 */
@Slf4j
public class BitOperatorsTest {

  @Test
  public void testSimple() {
    // 与 : 1 1 -> 1 否则 0
    assertThat(0b1010 & 0b1001, equalTo(0b1000));

    // 或 : 0 0 -> 0 否则 1
    assertThat(0b101 | 0b001, equalTo(0b101));

    // 异或: 不同 -> 1 否则 0
    assertThat(0b101 ^ 0b001, equalTo(0b100));

    // 非
    assertThat(~0b1110, equalTo(-0b1111));
    assertThat(~0b001, equalTo(-2));

    // 00000000000000000000000000000001
    show(0b001);
    // 11111111111111111111111111111110
    show(~0b001);// 以补码进行存储的, 所以取值时需要转回原码

    // 减一 11111111111111111111111111111101
    // 取反 10000000000000000000000000000010
    // 也就是  -2
  }

  @Test
  public void testBitAndAssignment() {
    int num = 0b101011;
    num |= 0b101;
    assertThat(num, equalTo(0b101111));

    // 101111 | 1011
    num |= num >>> 2;
    assertThat(num, equalTo(0b101111));
  }


  @Test
  public void testRight() {
    // 带符号的 右移操作 正数则左补0 负数则左补1
    assertThat(0b101 >> 2, equalTo(0b1));

    // 11111111111111111111111111111011 -0b101的补码表示(也是实际存储的值)
    show(-0b101);
    // 11111111111111111111111111111110 右移两位
    // 10000000000000000000000000000010 -1 取反 得到 原码
    assertThat(-0b101 >> 2, equalTo(-0b10));

    // 无视符号位的 右移操作 左补0
    assertThat(0b101 >>> 2, equalTo(0b1));
    assertThat(0b101 >>> 3, equalTo(0b0));

    // 11111111111111111111111111111011
    // 00000000000000000000000000000011
    show(-0b101 >>> 30);
    assertThat(-0b101 >>> 30, equalTo(0b11));

    // 在正数情况下 >> 等价于 >>>
    assertThat(0b110011 >> 3, equalTo(0b110011 >>> 3));
  }

  @Test
  public void testLeft() {
    assertThat(0b11_0110 << 2, equalTo(0b1101_1000));

    assertThat(0b1<<31, equalTo(0b1000_0000_0000_0000_0000_0000_0000_0000));
    show(0b1000_0000_0000_0000_0000_0000_0000_0000);

    // 溢出了32位...
    assertThat(0b1<<35, equalTo(0b1000));
    show(0b1<<35);
  }

  private static void show(int result) {
    log.info("{} {}", Integer.toBinaryString(result), result);
  }

  @Test
  public void testIdempotentOperation() {
    for (int i = 1; i < 45; i++) {
      int value = idempotentOperations(i);
      assertThat(value, equalTo((int) Math.pow(2, Math.floor(Math.log(i) / Math.log(2)) + 1)));
      System.out.println(i + " => " + value);
    }

    assertThat(idempotentOperations(0b1011_0000_0001_1111), equalTo(0b1_0000_0000_0000_0000));
    assertThat(idempotentOperations(0b10011_0000_0001_1111), equalTo(0b10_0000_0000_0000_0000));
  }

  @Test
  public void testCompareSpeed() {
    GetRunTime runTime = new GetRunTime().startCount();
    IntStream.rangeClosed(1, 10000).forEach(BitOperatorsTest::idempotentOperations);
    runTime.endCount("bit ");

    runTime.startCount();
    IntStream.rangeClosed(1, 10000)
        .forEach(i -> Math.pow(2, Math.floor(Math.log(i) / Math.log(2)) + 1));
    runTime.endCount("log and pow");

    // 后者性能优于前者, 因为虽然前者是位运算, 但是有太多多余操作
  }

  /**
   * 等价于 num => (int)Math.pow(2, Math.floor(Math.log(i) / Math.log(2)) + 1))
   * 最大使得形成 16位1 + 1
   */
  private static int idempotentOperations(int num) {
    int n = num;
    // 最高位肯定是1 右移一位,并做或操作然后最高位是两个1
    // 然后右移2位 最高位4个1,
    // 然后右移4位 ... 直到所有的位都是1. 所以中间的右移 3 5 6 7 都是多余的

    n |= n >>> 1; // 11xxx
    n |= n >>> 2; // 1111xxx
    n |= n >>> 4; // 11111111xx
    n |= n >>> 8; // 1111111111111111xx
    return n + 1;
  }
}
