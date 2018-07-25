package com.github.kuangcp.list;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-7-25-上午10:42
 */
@Slf4j
public class ArrayListTest {

  
  @Test
  public void testEmptyLoop(){
    List<String> list = new ArrayList<>();
    for (String temp : list) {
      log.debug("target: ={}", temp);
    }
  }
}
