package com.github.kuangcp.strcuture.stack;

/**
 * https://github.com/kuangcp
 * TODO test
 * @author kuangcp on 18-8-28-下午11:46
 */
public class MythArrayStack<T> implements MythBaseStack<T> {

  private int top = -1;
  private static final int DEFAULT_CAPACITY = 10;
  private int maxSize;
  private Object[] elementData;

  public MythArrayStack() {
    this(DEFAULT_CAPACITY);
  }

  public MythArrayStack(int maxSize) {
    this.maxSize = maxSize;
    elementData = new Object[maxSize];
  }

  @Override
  public void push(T data) {
    if (isFull()) {
      // TODO 扩容
      return;
    }
    top++;
    this.elementData[top] = data;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T pop() {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(0));
    }
    return (T) elementData[top];
  }

  private String outOfBoundsMsg(int index) {
    return "Index: " + index + ", Size: " + (top + 1);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T peek() {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(0));
    }
    return (T) elementData[top];
  }

  @Override
  public int size() {
    return top + 1;
  }

  @Override
  public boolean isEmpty() {
    return top == -1;
  }

  public boolean isFull() {
    return top == maxSize - 1;
  }

  @Override
  public void clear() {
    top = -1;
    elementData = new Object[DEFAULT_CAPACITY];
  }
}
