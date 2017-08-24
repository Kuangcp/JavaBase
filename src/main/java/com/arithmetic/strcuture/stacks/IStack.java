package com.arithmetic.strcuture.stacks;

/**
 * Created by https://github.com/kuangcp on 17-8-24  下午7:32
 */
public interface IStack {
    public void clear();
    public boolean isEmpty();
    public int length();
    public int peek();
    public void push(int data);
    //public void push (int data)throws Exception
    public int pop();

}


