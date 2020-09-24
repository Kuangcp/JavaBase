package syntax;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;
import org.junit.Test;

/**
 * @author https://github.com/Kuangcp
 * @date 2019-05-10 08:18
 */
public class InstanceOfTest {

  @Test
  public void test() {
    assertThat("" instanceof Object, equalTo(true));
    assertThat(new String() instanceof String, equalTo(true));
    assertThat(new Object() instanceof String, equalTo(false));
    // 如果左操作数 为 null 直接返回 false
    assertThat(null instanceof String, equalTo(false));
    assertThat((String) null instanceof String, equalTo(false));
    assertThat(new GenericClass<String>().isDateInstance(""), equalTo(false));

    // instanceof 只能用于对象 不能用于基本类型
//    assertThat('A' instanceof Character, equalTo(true));

    // 编译失败, 因为 instanceof 左右需要有继承或实现关系才能编译通过
//    assertThat(new Date() instanceof String, equalTo(false));

  }

  class GenericClass<T> {

    boolean isDateInstance(T t) {
      return t instanceof Date;
    }
  }


}
