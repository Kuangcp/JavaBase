package com.arithmetic.strcuture.stacks;

public interface IStack {
	public void clear();
	public boolean isEmpty();
	public int length();
	public int peek();
	public void push(int data);
	//ԭ���������ģ����ǻ�û�ú�ѧ�׳��쳣�����Ժ�������
	//public void push (int data)throws Exception
	public int pop();

}

