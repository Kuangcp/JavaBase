package com.github.kuangcp.ioc.initdestroy;

public class HelloWorld {

  public HelloWorld() {
    System.out.println("aaaa");
  }

  public void init() {
    System.out.println("init");
  }

  public void destroy() {
    System.out.println("destroy");
  }

  public void hello() {
    System.out.println("hello world");
  }
}
