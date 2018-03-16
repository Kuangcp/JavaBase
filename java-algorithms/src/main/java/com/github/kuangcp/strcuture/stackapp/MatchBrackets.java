package com.github.kuangcp.strcuture.stackapp;

import com.github.kuangcp.strcuture.stacks.LinkStack;
import com.github.kuangcp.strcuture.stacks.Node;

import java.util.Scanner;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:04
 * 要求：匹配算数表达式的括号，并指出哪里出错，就和编译器报错一样的
 *      这只是做到了检测括号，不能判断表达式整体是否正确，还简单的很呢，就做了一个晚上，加油！！
 *      下面的方法重载出问题了，留着以后来改
 */
public class MatchBrackets extends LinkStack {

    public static void main (String [] args){
        LinkStack S = new LinkStack ();
        int flag = 1;
        System.out.println("java");
        Scanner I = new Scanner(System.in);
        String buffer = I.nextLine();
        System.out.println("您输入的是："+buffer);
        I.close();

        for  (int k=0 ;k<buffer.length();k++){
            switch((char)(buffer.charAt(k))){
                case '(':S.push((char)(buffer.charAt(k)));break;
                case '{':S.push((char)(buffer.charAt(k)));break;
                case '[':S.push((char)(buffer.charAt(k)));break;
                case ')':
                    if (S.found('('))  S.pop();
                    else {System.out.println("多出右括号：）");flag=0;}break;
                case ']':
                    if (S.found('['))  S.pop();
                    else {System.out.println("多出右括号：]");flag=0;}break;
                case '}':
                    if (S.found('{'))  S.pop();
                    else {System.out.println("多出右括号：}");flag=0;}break;
                default :break;
            }
            //S.push((char)(buffer.charAt(k)));
        }


        if(!S.isEmpty()){

            while(!S.isEmpty()){
                flag=0;
                switch (S.pop()){
                    case '(':System.out.println("多出左括号：(");break;
                    case '{':System.out.println("多出左括号：{");break;
                    case '[':System.out.println("多出左括号：[");break;
                    default:System.out.println("什么鬼，你竟然能看到这句话");break;
                }
            }
        }
        //System.out.println(flag);
        //加这一行代码用于调试的根本原因就是条理不清晰，只考虑到后面的左括号多出，而忽略了前面那个右括号
        if (flag == 1) System.out.println("算术表达式括号匹配成功！！");

        //因为方法重写的失败，就另写了几行代码，代替display函数来遍历：
        Node p = S.top;
        while(p!=null){
            System.out.print((char)p.data);
            p = p.next;
        }

    }
	/*public void display(){
	Node p = this.top;
		while(p!=null){
			System.out.print((char)p.data);
			p = p.next;
		}//为什么方法重载失败！！！！！！！！！！！！！！！！！
	}*/
}


