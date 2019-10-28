package com.github.kuangcp.time.wheel;

import lombok.Data;

/**
 * @author https://github.com/kuangcp on 2019-10-28 13:10
 */
@Data
class Node {

  private String id;
  private Node next;
  private long runTime;

  Node(String id, Node next, long runTime) {
    this.id = id;
    this.next = next;
    this.runTime = runTime;
  }
}
