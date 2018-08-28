package com.github.kuangcp.strcuture.stack;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-28-上午12:08
 */
public interface MythBaseStack<T> {

  void push(T data);

  T pop();

  T peek();

  int size();

  boolean isEmpty();

  void clear();
}
