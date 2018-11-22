package com.github.kuangcp.forkjoin

/**
 *
 * @author kuangcp on 18-11-22-下午6:00
 */
class Element implements Comparable {

  Long id

  Element(Long id) {
    this.id = id
  }

  @Override
  int compareTo(Object other) {
    return this.id - ((Element) other).id
  }

  @Override
  String toString() {
    return "Element{" + "id=" + id + '}'
  }
}
