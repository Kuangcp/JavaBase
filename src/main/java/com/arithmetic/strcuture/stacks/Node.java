package com.arithmetic.strcuture.stacks;

public class Node {
	public int data;
	public Node next;
	public Node (){
		////this (null,null) 报错的说null不是Node类型
	}
	public Node(int data){
		this(data,null);
	}
	public Node(int data,Node next){
		this.data = data;
		this.next = next;
	}

}