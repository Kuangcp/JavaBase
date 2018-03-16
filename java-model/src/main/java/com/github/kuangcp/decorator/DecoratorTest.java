package com.github.kuangcp.decorator;

/**
 * 2016年下半年下午题最后一题 考装饰器
 * @author  Myth on 2016年11月12日 下午6:19:58
 * 错在了没有 super.ticket.print()
 * 因为这是ticket是父类的一个属性，如果不声明使用的话，嵌套的new 的话 地址指向不是同一个对象
 */
class Invocite{
	public void printInvocite(){
		System.out.println("正文");
	}
}

class Decorator extends Invocite{
	Invocite ticket = new Invocite();
	public Decorator(Invocite t){
		ticket = t;
	}
	public void ptintInvocite(){
		if(ticket!=null){
			ticket.printInvocite();
		}
	}
}

class HeadDecorator extends Decorator{
	public HeadDecorator(Invocite t){
		super(t);
	}
	public void printInvocite(){
		System.out.println("头");
		/////////////////////
		if(ticket!=null){
			super.ticket.printInvocite();
//			super.printInvocite();
		}
		////////////////////
	}
}

class FootDecorator extends Decorator{
	public FootDecorator(Invocite t){
		super(t);
	}
	public void printInvocite(){
		/////////////////
		if(ticket!=null){
			super.ticket.printInvocite();
//			super.printInvocite();
		}
		///////////////
		System.out.println("尾");
	}
}

public class DecoratorTest {
	public static void main(String []d){
		Invocite t = new Invocite();
		//////////////
		Invocite ticket = new FootDecorator(new HeadDecorator(t));
		/////////////
		ticket.printInvocite();
		System.out.println("----------------");
		//////////////
		ticket = new FootDecorator(new HeadDecorator(null));
		/////////////
		ticket.printInvocite();
	}
}
