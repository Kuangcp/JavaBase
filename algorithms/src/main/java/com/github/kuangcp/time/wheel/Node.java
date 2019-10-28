package com.github.kuangcp.time.wheel;

/**
 * @author https://github.com/kuangcp on 2019-10-28 23:53
 */
public interface Node {

  String getId();

  Node getNext();

  void setNext(Node nodeAble);
}
