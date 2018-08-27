package com.github.kuangcp.strcuture.stackapp;

import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-27-下午11:57
 */
public class MatchBracketTest {


  @Test
  public void testMatch() {
    new MatchBracket().match("[[[}]]]");
  }
}
