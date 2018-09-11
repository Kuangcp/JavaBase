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
 * @author kuangcp on 18-7-25-上午10:42
 */
@Ignore
@Slf4j
public class ArrayListTest {

  private List<String> one = new ArrayList<>();
  private List<String> two = new ArrayList<>();
  private int num = 100_000;


  @Test
  public void testEmptyLoop() {
    List<String> list = new ArrayList<>();
    for (String temp : list) {
      log.debug("target: ={}", temp);
    }
    int a = 1;
  }
  // TODO 集合的 交 差 并 补


  @Before
  public void initTwoList() {
    for (int i = 0; i < num; i++) {
      one.add("" + i);
      two.add("" + i + 4000);
    }
  }


  @Test
  public void testUnion() {
    GET_RUN_TIME.startCount();
    one.addAll(two);
    GET_RUN_TIME.endCount("union ");

    assertThat(one.size(), equalTo(2 * num));
  }

  // 交集
  @Test
  public void testInter() {
    GET_RUN_TIME.startCount();
    one.retainAll(two);

    GET_RUN_TIME.endCount("inter ");

  }

}
