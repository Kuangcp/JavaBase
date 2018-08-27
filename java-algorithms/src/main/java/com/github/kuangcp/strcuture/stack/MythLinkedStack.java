package com.github.kuangcp.strcuture.stack;

import java.util.Objects;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-28-上午12:08
 */
public class MythLinkedStack<T> implements MythBaseStack<T> {

  private Node top = null;

  class Node<T> {

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

    Node<T> newNode = new Node<>(data);
    if (!Objects.isNull(top)) {
      newNode.next = top;
    }
    top = newNode;
  }

  @Override
  public T pop() {
    top = top.next;
    return peek();
  }

  @Override
  public T peek() {
    return (T) top.getData();
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void clear() {

  }
}
