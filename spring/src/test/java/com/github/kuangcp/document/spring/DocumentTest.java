package com.github.kuangcp.document.spring;

import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;

public class DocumentTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    DocumentManager documentManager = (DocumentManager) context.getBean("documentManager");
    documentManager.read();
  }
}
