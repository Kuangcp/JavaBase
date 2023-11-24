package com.github.kuangcp.set;

import org.junit.Test;

import java.util.Arrays;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
/**
 * @author https://github.com/kuangcp
 * @date 2019-05-24 09:22
 */
public class TreeSetTest {

  @Test
  public void testSort() {
    // 获取第二大元素
    Integer[] nums = {1, 2, 432, 4523, 5433452, 34523452, 4573, 33421, 3456, 8567, 342, 76, 1234};
    TreeSet<Integer> treeSet = new TreeSet<>(Arrays.asList(nums));
    Integer result = treeSet.lower(treeSet.last());
    System.out.println(treeSet);

    assertThat(result, equalTo(5433452));
  }
}
