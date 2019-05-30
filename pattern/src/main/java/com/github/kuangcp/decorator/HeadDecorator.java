package com.github.kuangcp.decorator;

/**
 * @author kuangcp on 4/6/19-6:29 PM
 */
class HeadDecorator extends Decorator {

  public HeadDecorator(Invoice t) {
    super(t);
  }

  public void printInvoice() {
    System.out.println("head");
    // 2
    super.printInvoice();
  }
}
