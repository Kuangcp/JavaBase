package com.github.kuangcp.decorator;

import org.junit.Test;

/**
 * 2016年软考下半年下午题最后一题 考装饰器
 *
 * @author Myth on 2016年11月12日 下午6:19:58
 */
public class DecoratorTest {

  @Test
  public void test() {
    Invoice invoice = new Invoice();
    // 4
    Invoice ticket = new FootDecorator(new HeadDecorator(invoice));
    ticket.printInvoice();

    System.out.println("----------------");
    // 5
    ticket = new FootDecorator(new HeadDecorator(null));
    ticket.printInvoice();
  }
}
