package com.github.kuangcp.di.annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public class Person {

  @Resource(name = "student")
  private Student student;

  @Resource
  private Long pid;

  public void say() {
    this.student.say();
  }

  @PostConstruct
  public void init() {
    System.out.println("init");
  }

  @PreDestroy
  public void destroy() {
    System.out.println("destroy");
  }
}
