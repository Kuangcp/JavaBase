package base.enums;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 * https://mp.weixin.qq.com/s/aGMz1u0Oh4ZHTDBFvgq0lg
 * TODO 测试枚举类实现的单例 是否真的安全
 * @author kuangcp
 */
public class StringToolTest {


  @Test
  public void setUp() {
    String result = StringTool.INSTANCE.getTool().up("fd");
    System.out.println(result);
    System.out.println(StringTool.INSTANCE.getTool());
    System.out.println(StringTool.INSTANCE.getTool());
    System.out.println(StringTool.INSTANCE.getTool());
    System.out.println(StringTool.INSTANCE.getTool());
    System.out.println(StringTool.INSTANCE.getTool());

  }
}
