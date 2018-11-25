package com.github.kuangcp.simple;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:45
 * 测试泛型方法
 *
 * @author kuangcp
 */
public class SimpleMethodTest {

  /**
   * 和核心技术卷上不一样,应该是版本的问题, 入参就已经限制成数组了,怎么可能会有别的类型混进来
   * 这里在调用的时候就可以看到泛型起作用了，传入的参数类型是和结果类型一致的
   */
  @Test
  public void testGetMiddle() {
    Float[] arrays = {2.1f, 4.2f, 3.5f};
    Float result = SimpleMethod.getMiddle(arrays);
    assert result == 4.2f;
  }

  /**
   * 意义在于 放入一个同类型的数组,得到最大值,只要这个类型实现了 Comparable Serializable 即可
   */
  @Test
  public void testGetMax() {
    Score[] stus = {
        new Score(80f, 50f),
        new Score(70f, 80f),
        new Score(100f, 70f)
    };
    Score result = SimpleMethod.getMax(stus);
    assert result.getNormal() == 100;
  }
}