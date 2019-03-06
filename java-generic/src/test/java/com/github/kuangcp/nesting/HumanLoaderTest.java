package com.github.kuangcp.nesting;

import java.util.Map;
import org.junit.Test;

/**
 * @author kuangcp on 3/6/19-3:27 PM
 */
public class HumanLoaderTest {


  @Test
  public void testLoad(){
    HumanLoader humanLoader = new HumanLoader();
    humanLoader.load("xx");

    Map<String, HumanVO> data = humanLoader.getMap();
  }
}