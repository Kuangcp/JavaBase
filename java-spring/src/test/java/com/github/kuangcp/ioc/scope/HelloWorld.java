package com.github.kuangcp.ioc.scope;

import java.util.ArrayList;
import java.util.List;

public class HelloWorld {

  /**
   * 该属性是共享的
   * 如果该属性有数据，会引发线程安全问题
   */
  private List<String> lists = new ArrayList<String>();

  public HelloWorld() {
    System.out.println("aaaa");
  }

  public void hello() {
    System.out.println("hello world");
  }

  public List<String> getLists() {
    return lists;
  }
}
