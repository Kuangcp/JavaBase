package com.github.kuangcp.time.wheel;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-10-28 23:53
 */
public interface Node {

  String getId();

  Node getNext();

  void setNext(Node nodeAble);
}
