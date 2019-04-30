package com.github.kuangcp.simpleMethod.SimplexMethodQuarter;

import com.github.kuangcp.math.number.Fraction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Created by Myth on 2017/3/23 0023
 */
public class MethodTest {

  @Test
  public void testMax() {
    List<Fraction> list = new ArrayList<>();

    list.add(new Fraction(-1, 3));
    list.add(new Fraction(-15, 1));
    list.add(new Fraction(5, 2));
    SimplexMethod sm = new SimplexMethod();
    Integer index = sm.maxList(list, false, true, false);
    if (index != -1) {
      System.out.println(index + " => " + list.get(index));
    } else {
      System.out.println("没有合适的数据");
    }
    for (Fraction t : list) {
      System.out.println(t.toString());
    }
  }

  @Test
  public void testMap() {
    Map<String, String> temp = new HashMap<String, String>();
    temp.put("2", "3");
    if (!temp.containsKey("2")) {
      temp.put("2", "5");
    } else {
      System.out.println("All");
    }
    for (String key : temp.keySet()) {
      System.out.println(temp.get(key));
    }
  }
}
