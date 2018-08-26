package com.github.kuangcp.strcuture.stacks;

/**
 * 数组栈，top指向栈顶后一个元素即实际长度
 */
public class Stack implements IStack {

  private int top = -1;
  static int maxsize;
  private int[] StackElem;

  void SqStack(int max) {
    top = 0;
    StackElem = new int[max];
  }

  public void clear() {
    top = 0;
  }

  private boolean isfull() {
    return top >= maxsize;
  }

  public boolean isEmpty() {
    return top == 0;
  }

  public int length() {
    return top;
  }

  public int peek() {
    if (!isEmpty()) {
      return StackElem[top - 1];
    } else {
      return -999;//本该抛出异常的
    }
  }

  public void push(int data) {
    if (!isfull()) {
      StackElem[top + 1] = data;//这里忘了加一了，结果第一个元素跑到最后了，为什么？
      top++;
    }
  }

  public int pop() {
    if (!isEmpty()) {
      return StackElem[top--];
    } else {
      return -999;//该抛出异常
    }
  }

  public void display() {
    int p = top;
    while (p != 0) {//这里用isEmpty就会出错，在对象中是可以使用的，类中就会出现一直不空的情况死循环
      System.out.println(StackElem[p--]);
    }
  }

}

