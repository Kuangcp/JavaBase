package com.github.kuangcp.queue.use.blocking;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ToString
class Cat extends Pet {


  Cat(String name) {
    super(name);
  }

  @Override
  void examine() {
    log.info("cat: " + name);
  }


}