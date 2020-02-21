package com.github.kuangcp.document;

public class ExcelDocument implements Document {

  @Override
  public void read() {
    System.out.println("excel read");
  }

  @Override
  public void write() {
    System.out.println("excel writer");
  }

}
