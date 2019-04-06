package com.github.kuangcp.decorator;

/**
 * @author kuangcp on 4/6/19-6:29 PM
 */
class Decorator extends Invoice {

  private Invoice ticket;

  Decorator(Invoice t) {
    ticket = t;
  }

  public void printInvoice() {
    if (ticket != null) {
      // 1
      ticket.printInvoice();
    }
  }
}
