package com.github.kuangcp.found;

import com.github.kuangcp.sort.Insert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-27-上午12:07
 */
@Slf4j
public class BinarySearchTest {

  private int dataBaseValue = 10;
  private int dataRange = 90;
  private int dataScale = 100;

  @Test
  public void testFind() {
    BinarySearch s = new BinarySearch();
    int[] dat = new int[dataScale];

    for (int i = 0; i < dat.length; i++) {
      dat[i] = (int) (Math.random() * dataRange + dataBaseValue);
    }

    Insert.sort(dat);

    for (int i = 0; i < dat.length; i++) {  //将数组遍历一下
      System.out.print(dat[i] + " ");
      if ((i + 1) % 10 == 0) {
        System.out.println();
      }
    }

    int randomValue = (int) (Math.random() * dataRange + dataBaseValue);
    int result = s.find(dat, randomValue);
    if (result != -1) {
      log.debug("你要找的数据是第 {} 个数字 {}", result, randomValue);
    } else {
      log.debug("该数据不存在，查找失败！value={}", randomValue);
    }
  }
}