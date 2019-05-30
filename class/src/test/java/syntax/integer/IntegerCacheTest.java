package syntax.integer;

import org.junit.Test;

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
}
