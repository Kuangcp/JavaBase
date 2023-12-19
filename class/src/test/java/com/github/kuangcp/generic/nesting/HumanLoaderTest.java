package com.github.kuangcp.generic.nesting;

import org.junit.Test;

import java.util.Map;

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