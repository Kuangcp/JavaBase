package com.github.kuangcp.queue.use.blocking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
class Cat implements Pet {

  private String name;

  Cat(String name) {
    this.name = name;
  }

  @Override
  public void examine() {
    log.info("examine cat: " + name);
  }
}