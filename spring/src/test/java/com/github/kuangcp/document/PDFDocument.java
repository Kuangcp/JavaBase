package com.github.kuangcp.document;

public class PDFDocument implements Document {

  @Override
  public void read() {
    System.out.println("pdf read");
  }

  @Override
  public void write() {
    System.out.println("pdf write");
  }

}
