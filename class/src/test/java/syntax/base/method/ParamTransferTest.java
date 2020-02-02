package syntax.base.method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/11/19-10:21 PM
 */
public class ParamTransferTest {

  static class A {

    private String name;

  }

  private void modifyName(A a) {
    a.name = "2";
    assertThat(a.name, equalTo("2"));

    // 只更改到形参不能更改实参引用指向
    a = new A();
    a.name = "3";
    assertThat(a.name, equalTo("3"));
  }

  @Test
  public void testMain() {
    A a = new A();
    a.name = "1";

    modifyName(a);
    assertThat(a.name, equalTo("2"));
  }
}
