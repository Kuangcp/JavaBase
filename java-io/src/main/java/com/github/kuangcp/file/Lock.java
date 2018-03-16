package com.github.kuangcp.file;

import java.util.Scanner;

/*
 * 密码锁 combination lock：
 * 密码（三个数字的序列）是隐藏的，通过密码可以解锁，通过组合可以改变密码
 * 但只有当前知道密码的人才能改变密码，设计一个类，具有公有方法open，changeCombo
 * 和保存密码的私有数据字段。密码应该在构造函数中
 */
class LockUse{
	
    String file;//密码保护的内容
	 boolean pass = false;//记录是否正确输入密码
	 private String code;//密码
	
	public void Pass(String s){//判断密码是否正确
		if (s.equals(code)){
			this.pass = true;
			System.out.println("密码正确！");
		}
		else {this.pass = false;System.out.println("密码错误！");}//this.pass = false;
		//这句话就是不能少的，当你更改密码后是不能缺少的重置状态语句
	}
	public void openfile(){//打开保护的文件
		if (this.pass){System.out.println("内容就是："+this.file);}
		else System.out.println("@@@请先验证密码！！！");
	}
	public void changeCombo(String str){//更改密码
		 if (this.pass){
			 this.code = str; 
		 }
		 else {System.out.println("_________请先验证密码：");
		 Scanner s = new Scanner(System.in);String strs;
		 while (!this.pass){System.out.println("请输入密码：");
			strs = s.nextLine();
			this.Pass(strs);
			 }
		 }
		 
	}
	public LockUse(String file){//构造器初始化文件，密码
		this.file = file;
		this.code = "123";
	}
}

public class Lock {//用于实现的类
	
	public static void main(String[] args) {
	    //while (menu()){} 
		//不能用函数多次调用，因为每次调用都是一个新的栈，就算登录了系统但是没有被保存登录状态
		menu();
	    System.out.println("您已经退出系统！");
	}
	public static void menu(){
		LockUse Q =  new LockUse("你居然知道了密码");//  创建对象总是忘了加 new关键字！！！！！！！！！！！！！
		String strs;
		Scanner s = new Scanner(System.in);
		
		while (true){
			System.out.println("\n##请输入要做的操作：\n##1.验证密码\n##2.打开文件\n##3.更改密码");
			strs = s.nextLine();
			
			switch (strs){
				case "1":{
					System.out.println("请输入密码：");
					strs = s.nextLine();
					Q.Pass(strs);}break;
				case "2":
					Q.openfile();break;
				case "3":{
					System.out.println("请输入您要更改的密码：");
					String p = s.nextLine();
					Q.changeCombo(p);
					}break;
				default : System.out.println("请输入0-2的选择！");break;
			}
			
		}
	}
	
}

