package com.github.kuangcp.document;

public class WordDocument implements Document {

  @Override
  public void read() {
    System.out.println("word read");
  }

  @Override
  public void write() {
    System.out.println("word write");
  }

}
