package com.github.kuangcp.decorator;

/**
 * @author kuangcp on 4/6/19-6:29 PM
 */
class FootDecorator extends Decorator {

  public FootDecorator(Invoice t) {
    super(t);
  }

  public void printInvoice() {
    // 3
    super.printInvoice();
    System.out.println("foot");
  }
}
