package com.github.kuangcp.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * TODO list 发生 并发修改异常时的处理方式
 *
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class ConcurrentModificationTest {

  private List<String> list = new ArrayList<>(10);

  @Test
  public void testGenerate() {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals("del")) {
        list.remove(i);
      }
    }
  }

  @Test
  public void testForEach() {
    for (String x : list) {
      if (x.equals("del")) {
        list.remove(x);
      }
    }
  }
  
  @Test
  public void testIterator(){
    list.removeIf(x -> x.equals("del"));

    Iterator<String> it = list.iterator();
    while(it.hasNext()){
      String x = it.next();
      if(x.equals("del")){
        it.remove();
      }
    }
  }
}
