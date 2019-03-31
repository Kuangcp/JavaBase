package com.github.kuangcp.key;

import org.testng.annotations.Test;

/**
 * @author kuangcp on 3/31/19-2:56 PM
 */
public class CreateKeyTest {

  @Test
  public void testUse() {
    CreateKey keyMaker = new CreateKey();
//        System.out.println(keyMaker.genericPyCharmKey(1, 13, "Myth"));
//        System.out.println(keyMaker.genericPhpStormKey(1, 13, "Myth"));
//        System.out.println(keyMaker.genericRubyMineKey(1, 13, "Myth"));
    System.out.println(keyMaker.genericWebStormKey(1, 13, "Myth"));
  }
}