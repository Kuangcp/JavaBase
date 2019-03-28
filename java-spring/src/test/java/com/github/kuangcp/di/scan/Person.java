package com.github.kuangcp.di.scan;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("perso")
public class Person {

  @Resource(name = "student")
  private Student studen;

  private Long pid;

  public void say() {
    this.studen.say();
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
