package com.github.kuangcp.time.wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author https://github.com/kuangcp on 2019-10-28 13:10
 */
class LinkedList<T extends Node> {

  private T head;
  private T tail;
  private int len;

  boolean add(T node) {
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

  List<T> toList() {
    List<T> result = new ArrayList<>(len);
    T pointer = this.head;
    while (Objects.nonNull(pointer)) {
      result.add(pointer);
      pointer = (T)pointer.getNext();
    }
    return result;
  }

  String toSimpleString() {
    List<T> nodes = toList();
    if (nodes.isEmpty()) {
      return "";
    }
    return nodes.stream().map(T::getId).collect(Collectors.joining(","));
  }

  boolean isEmpty() {
    return Objects.isNull(head);
  }
}
