package com.github.kuangcp.ioc.createobject.when;

import java.util.ArrayList;
import java.util.List;

public class Person {

  private List<String> lists = new ArrayList<String>();

  public Person() {
    for (int i = 0; i < 10; i++) {
      lists.add(i + "");
    }
    System.out.println("person");
  }

  public void say() {
    System.out.println("aaaa");
  }
}
