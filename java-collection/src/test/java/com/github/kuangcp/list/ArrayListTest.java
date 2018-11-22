package com.github.kuangcp.list;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 交 差 并 补
 * @author kuangcp on 18-7-25-上午10:42
 */
@Ignore
@Slf4j
public class ArrayListTest {

  private List<String> one = new ArrayList<>();
  private List<String> two = new ArrayList<>();

  private int num = 100;
  private int start = 40;

  @Before
  public void initTwoList() {
    for (int i = 0; i < num; i++) {
      one.add("" + i);
      two.add("" + (i + start));
    }
  }

  // 并集 A + B - A 交 B
  @Test
  public void testUnion() {
    GET_RUN_TIME.startCount();

    one.removeAll(two);
    assertThat(one.size(), equalTo(start));
    
    one.addAll(two);
    assertThat(one.size(), equalTo(num + start));

    GET_RUN_TIME.endCount("union ");
  }

  // 交集
  @Test
  public void testInter() {
    GET_RUN_TIME.startCount();

    one.retainAll(two);
    assertThat(one.size(), equalTo(num - start));

    GET_RUN_TIME.endCount("inter ");
  }

  // 差集 (A 并 B) - (A交B)
  @Test
  public void testDifference() {
    GET_RUN_TIME.startCount();

    one.retainAll(two);
    assertThat(one.size(), equalTo(num - start));

    one.addAll(two);
    assertThat(two.size(), equalTo(num));

    GET_RUN_TIME.endCount("Difference");
  }


  // 补集 A 是 S 的子集, S - A 就是A的补集
  @Test
  public void testComplement(){
   GET_RUN_TIME.startCount();

    ArrayList<String> three = new ArrayList<>(one);
    three.removeAll(two);
    assertThat(three.size(), equalTo(start));
    one.removeAll(three);
    assertThat(one.size(), equalTo(num - start));

    // 这里 one 是 two 的 子集
    two.removeAll(one);
    // two 是上面的 one 在 原来 two 的补集
    assertThat(two.size(), equalTo(start));

    GET_RUN_TIME.endCount("Complement");
  }
}
