package com.arithmetic.strcuture.stacks;

public class LinkStackTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkStack p =new LinkStack();
		for (int i=0;i<23;i++)
		p.push(i);
		
		System.out.println("�����ǣ�"+p.length());
		p.display();

	}

}
