package com.github.kuangcp.di.autoscan;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("person")
public class Person {

  @Resource(name = "student")
  private Student student;

  private Long pid;

  public void say() {
    this.student.say();
  }

  @PostConstruct
  public void init() {
    log.info("init");
  }

  @PreDestroy
  public void destroy() {
    log.info("destroy");
  }
}
