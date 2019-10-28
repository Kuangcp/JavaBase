package com.github.kuangcp.time.wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author https://github.com/kuangcp on 2019-10-28 13:10
 */
class LinkedList {

  private Node head;
  private Node tail;
  private int len;

  boolean add(Node node) {
    len++;
    if (Objects.isNull(head)) {
      head = node;
      tail = node;
      return true;
    }

    this.tail.setNext(node);
    this.tail = node;
    return true;
  }

  void clear() {
    len = 0;
    this.head = null;
    this.tail = null;
  }

  List<Node> toList() {
    List<Node> result = new ArrayList<>(len);
    Node pointer = this.head;
    while (Objects.nonNull(pointer)) {
      result.add(pointer);
      pointer = pointer.getNext();
    }
    return result;
  }

  String toSimpleString() {
    List<Node> nodes = toList();
    if (nodes.isEmpty()) {
      return "";
    }
    return nodes.stream().map(Node::getId).collect(Collectors.joining(","));
  }

  boolean isEmpty() {
    return Objects.isNull(head);
  }
}
