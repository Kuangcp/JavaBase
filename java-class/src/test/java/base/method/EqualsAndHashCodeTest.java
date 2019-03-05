package base.method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/5/19-11:17 AM
 * 值类 必须重写 equals 使得比较时比较的是所有的值而不是地址
 * 其他普通类 不重写
 * 或者不暴露 equals 方法就重写并抛出异常
 */
@Slf4j
public class EqualsAndHashCodeTest {

  abstract class Flyable {

  }

  // 当一个类继承抽象类时, Lombok 的 EqualsAndHashCode 注解会有警告, 提醒你要使用Object的equals还是lombok重写的equals
  @Data
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  class Target extends Flyable {

    private String name;
    private int age;

  }

  @Test
  public void testEquals() {
    Target target = new Target("you", 3);
    Target you = new Target("you", 3);

    log.info("hasCode: target={} you={}", target.hashCode(), you.hashCode());
    log.info("equals={}", target.equals(you));

    assertThat(target.hashCode(), equalTo(you.hashCode()));
  }
}


