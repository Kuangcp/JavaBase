package com.github.kuangcp.function.sort;


import static com.github.kuangcp.time.GetRunTime.INSTANCE;

import java.util.ArrayList;
import java.util.Comparator;
import org.junit.Test;

/**
 * @author kuangcp on 18-8-17-下午12:00
 * base data type use functional Comparator to sort
 */
public class BaseTypeSortTest {

  @Test
  public void testInt() {
    int[] array = {2, 4, 5, 3, 1, 5, 6, 8, 3, 6, 7, 12, 556};

    ArrayList<Integer> lists = new ArrayList<>();
    for (int num : array) {
      lists.add(num);
    }
    INSTANCE.startCount();

    lists.sort(Comparator.comparingInt(Integer::intValue));
//    lists.forEach(System.out::println);
    INSTANCE.endCount("排序完成");
  }
  
  @Test
  public void testLong(){
    
  }
  
  @Test
  public void testDouble(){
    
  }
}
