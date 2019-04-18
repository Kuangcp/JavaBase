package com.github.kuangcp.queue.use.blocking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
class Appointment<T> {

  private final T toBeSeen;

  T getPatient() {
    log.info("handle " + toBeSeen.toString());
    return toBeSeen;
  }

  Appointment(T incoming) {
    toBeSeen = incoming;
  }
}