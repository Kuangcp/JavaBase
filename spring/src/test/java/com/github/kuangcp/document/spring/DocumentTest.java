package com.github.kuangcp.document.spring;

import com.github.kuangcp.util.SpringHelper;
import org.junit.Test;

public class DocumentTest extends SpringHelper {

  static {
    path = "cn/itcast/spring0909/document/spring/applicationContext.xml";
  }

  @Test
  public void test() {
    DocumentManager documentManager = (DocumentManager) context.getBean("documentManager");
    documentManager.read();
  }
}
