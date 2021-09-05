/**
 * 一个线程对象只能启动一个线程
 * 一个线程只能启动一次
 * 
 */

package com.github.kuangcp.tank.v2;

public class Thread3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Cat2 cat1 = new Cat2();
		cat1.start();
//		cat1.start();  这样启动一个线程是要报错的
		
		Dog2  dog1 = new Dog2();
		Thread t = new Thread(dog1);
		Thread t2 = new Thread(dog1);
		t.start();
//		t.start(); 这样也是要报错的，不允许启动两次
		t2.start();
		//这样启动线程不会报错，因为是两个线程的对象t和t2，虽然是封装的是同一个对象dog1
		
	}

}

class Cat2 extends Thread {
	public void run(){
		System.out.println("猫");
	}
}

class Dog2 implements Runnable {
	public void run(){
		System.out.println("狗");
	}
}