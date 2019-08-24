package com.github.kuangcp.bloom.filter;

import org.junit.Test;

/**
 * @author kuangcp on 2019-08-18 下午12:06
 */
public class HashFunctionsTest {

  @Test
  public void test() {
    String id = "d4b02db2-6fb2-48e5-925f-44223f7f43e4";

    Integer result = HashFunctions.hashWithString.apply(id);
    System.out.println(result);

  }
}