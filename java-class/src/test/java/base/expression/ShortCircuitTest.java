package base.expression;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-30-上午10:43
 * short circuit with expression : and or
 */
@Slf4j
public class ShortCircuitTest {

  private int flag;

  private boolean addValue() {
    flag++;
    return true;
  }

  @Test
  public void testAnd() {
    // first satisfied, invoke second
    if (flag == 0 && addValue()) {
      assertThat(flag, equalTo(1));
    }

    // first expression is not satisfied conditions, then second expression not invoke
    if (flag == 2 && addValue()) {
      assertThat(flag, equalTo(1));
    }
  }

  @Test
  public void testOr() {
    // first expression is satisfied conditions, then second expression not invoke
    if (flag == 0 || addValue()) {
      assertThat(flag, equalTo(0));
    }

    // first not satisfied, invoke second
    if (flag == 2 || addValue()) {
      assertThat(flag, equalTo(1));
    }
  }
}
