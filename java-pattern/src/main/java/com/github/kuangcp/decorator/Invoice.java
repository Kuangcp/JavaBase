package com.github.kuangcp.decorator;

/**
 * 2016年下半年下午题最后一题 考装饰器
 *
 * @author Myth on 2016年11月12日 下午6:19:58
 * 错在了没有 super.ticket.print()
 * 因为这是ticket是父类的一个属性，如果不声明使用的话，嵌套的new 的话 地址指向不是同一个对象
 */
class Invoice {

  public void printInvoice() {
    System.out.println("content");
  }
}
