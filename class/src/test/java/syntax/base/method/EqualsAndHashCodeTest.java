package syntax.base.method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp
 *
 * 值类 必须重写 equals 使得比较时比较的是所有属性的值而不是地址 其他普通类 不重写
 */
@Slf4j
public class EqualsAndHashCodeTest {

  abstract class Flyable {

  }

  // 当一个类继承抽象类时, Lombok 的 EqualsAndHashCode 注解会有警告
  // 提醒你要使用Object的equals还是lombok重写的equals
  @Data
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  private class Target extends Flyable {

    private String name;
    private int age;
  }

  // 若 hashCode 相等,则 不一定满足 equals;
  // 若满足 equals, 则 hashCode一定相等
  @Test
  public void testEquals() {
    Target target = new Target("you", 3);
    Target you = new Target("you", 3);

    log.info("hasCode: target={} you={}", target.hashCode(), you.hashCode());
    log.info("equals={}", target.equals(you));

    assertThat(target.hashCode(), equalTo(you.hashCode()));
  }

  class Electronics {

    String name;

    Electronics() {
    }

    Electronics(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
      // 如果改成 用 类去判断 就能避免问题
      // if(Objects.nonNull(obj) && obj.getClass() == this.getClass())
      if (Objects.nonNull(obj) && obj instanceof Electronics) {
        Electronics phone = (Electronics) obj;
        return phone.name.equals(name);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  class Phone extends Electronics {

    private int number;

    Phone(int number, String name) {
      this.number = number;
      this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Phone) {
        Phone phone = (Phone) obj;
        return super.equals(obj) && phone.number == number;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  @Test
  public void testEqualPhone() {
    Phone a = new Phone(12, "HONOR");
    Phone b = new Phone(1777, "HONOR");

    Electronics electronics = new Electronics("HONOR");

    // 这里是因为 父类 使用的 instanceof, 子类自然能通过判断
    assertThat(electronics.equals(b), equalTo(true));
    assertThat(electronics.equals(a), equalTo(true));

    assertThat(a.equals(b), equalTo(false));
  }
}


