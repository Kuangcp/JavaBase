package com.github.kuangcp.queue.use.blocking;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
class Dog extends Pet {


  Dog(String name) {
    super(name);
  }

  @Override
  void examine() {
    log.info("dog : " + name);
  }
}