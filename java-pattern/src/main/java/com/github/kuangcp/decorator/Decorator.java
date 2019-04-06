package com.github.kuangcp.decorator;

abstract class Decorator extends Invoice {

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
