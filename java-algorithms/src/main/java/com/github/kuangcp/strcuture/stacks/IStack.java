package com.github.kuangcp.strcuture.stacks;

/**
 * Created by https://github.com/kuangcp on 17-8-24  下午7:32
 */
public interface IStack {

  void clear();

  boolean isEmpty();

  int length();

  int peek();

  void push(int data);

  //public void push (int data)throws Exception
  int pop();

}


