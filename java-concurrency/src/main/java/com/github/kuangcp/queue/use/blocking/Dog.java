package com.github.kuangcp.queue.use.blocking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
class Dog implements Pet {

  private String name;

  Dog(String name) {
    this.name = name;
  }

  @Override
  public void examine() {
    log.info("examine dog : " + name);
  }
}