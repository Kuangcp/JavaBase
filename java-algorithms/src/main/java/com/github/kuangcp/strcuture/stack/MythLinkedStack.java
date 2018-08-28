package com.github.kuangcp.strcuture.stack;

import java.util.Objects;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-28-上午12:08
 */
public class MythLinkedStack<T> implements MythBaseStack<T> {

  private Node top = null;

  class Node {

    T data;
    Node next;

    public Node(T data) {
      this.data = data;
      this.next = null;
    }

    public Node(T data, Node next) {
      this.data = data;
      this.next = next;
    }

    public T getData() {
      return this.data;
    }
  }

  @Override
  public void push(T data) {
    Node newNode = new Node(data);
    if (!Objects.isNull(top)) {
      newNode.next = top;
    }
    top = newNode;
  }

  @Override
  public T pop() {
    if (isEmpty()) {
      return null;
    }
    T temp = top.getData();
    top = top.next;
    return temp;
  }

  @Override
  public T peek() {
    if (Objects.isNull(top)) {
      return null;
    }
    return top.getData();
  }

  @Override
  public int size() {
    Node temp = top;
    if (Objects.isNull(temp)) {
      return 0;
    }
    int count = 0;
    while (!Objects.isNull(temp)) {
      count++;
      temp = top.next;
    }
    return count;
  }

  @Override
  public boolean isEmpty() {
    return Objects.isNull(top);
  }

  @Override
  public void clear() {
    top = null;
  }
}
