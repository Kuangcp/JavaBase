package com.github.kuangcp.di.autoscan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Student {

  public void say() {
    log.info("student");
  }
}
