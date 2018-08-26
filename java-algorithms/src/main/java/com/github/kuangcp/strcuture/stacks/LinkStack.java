package com.github.kuangcp.strcuture.stacks;

/**
 * 链栈，top指向当前栈顶元素 完全是C的写法
 *
 */
public class LinkStack implements IStack {

  public Node top;//top只是指针而已

  public void clear() {
    top = null;
  }

  public int length() {//求链栈的长度
    int length = 0;
    Node p = top;
    while (p != null) {
      length++;
      p = p.next;
    }
    return length;
  }

  public boolean isEmpty() {//判断链栈是否为空
    return top == null;
  }

  public int peek() {
    if (!isEmpty()) {
      return top.data;
    } else {
      return -999;//本该抛出异常的
    }
  }

  public void push(int data) {//进栈
    //Node p = new Node(data,top);//p就在top之后了
    Node p = new Node(data);
    if (top != null) {
      p.next = top;
    }
    top = p;
  }

  public int pop() {//出栈
    int a;
    a = top.data;
    top = top.next;
    return a;
  }

  public String display() {//遍历链栈
    StringBuilder result = new StringBuilder();
    Node p = top;
    while (p != null) {
      result.append(p.data);
//      System.out.print(p.data);
      p = p.next;
    }
    return result.toString();
  }

  public boolean found(char c) {//在栈中查找指定字符
    Node p = top;
    while (p != null) {
      if (p.data == c) {
        return true;
      }
      p = p.next;
    }
    return false;
  }
	/*public void display1(){
		if (!isEmpty()){
			do
			{
				System.out.print(top.data);
				top = top.next;//这里对top进行 了改动，所以判空函数被影响，不过为什么报错？？
			}while(top.next == null);
		}
	}*/
}
 

