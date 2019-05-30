package com.github.kuangcp.forkjoin

/**
 *
 * @author kuangcp on 18-11-22-下午6:01
 */
class Counter {

  int size = 0

  synchronized void increase() {
    size++
  }

  int current() {
    return size
  }
}
