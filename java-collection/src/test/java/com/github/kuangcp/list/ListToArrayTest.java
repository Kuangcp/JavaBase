package com.github.kuangcp.list;

import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午3:02
 */
@Slf4j
public class ListToArrayTest {

  /**
   * 测试toArray方法，得到的结论就是 无参数那个是复制一份返回一个等大的数组回来
   * 有数组做参数那个是将传入的数组作为容器，将调用方这个数组里的数据复制一份到这个数组里去，
   * 但是若空间小了就采取无参的做法新建一个
   * 若空间大了 那么会将结果数组最后一个元素之后的元素设置为 null。
   */
  @Test
  public void toArrayMethod() {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      list.add(i);
    }
    log.debug("{}", list);
    Object[] copy1 = list.toArray();
    log.debug(Arrays.toString(copy1));

    Integer[] target = new Integer[10];
    log.debug(Arrays.toString(target));
    Object[] copy2 = list.toArray(target);
    log.debug(Arrays.toString(copy2));

    Integer[] target2 = new Integer[5];
    log.debug(Arrays.toString(target2));
    Object[] copy3 = list.toArray(target2);
    log.debug(Arrays.toString(copy3));
  }

}
