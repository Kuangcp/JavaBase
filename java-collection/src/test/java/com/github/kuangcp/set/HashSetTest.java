package com.github.kuangcp.set;

import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/7/19-12:47 AM
 */
@Slf4j
public class HashSetTest {

  // HashSet 就是持有了HashMap对象, 把元素作为key存进去, value则是共用一个默认对象
  @Test
  public void testNew(){
    HashSet<String> set = new HashSet<>();

  }
}
