package com.github.kuangcp.jdbc.jdbc;

import com.github.kuangcp.common.SpringHelper;
import javax.sql.DataSource;
import org.junit.Test;

public class DataSourceTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void testDataSource() {
    /**
     * 完全的面向接口编程 面向接口方便雨注入属性。减少了代码量
     */
    DataSource dataSource = (DataSource) context.getBean("dataSource");
    System.out.println(dataSource);
  }
}
