package com.github.kuangcp.aop.xml;


import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;

/**
 * 原理
 * *  加载配置文件，启动spring容器
 * *  spring容器为bean创建对象
 * *  解析aop的配置，会解析切入点表达式
 * *  看纳入spring管理的那个类和切入点表达式匹配，如果匹配则会为该类创建代理对象
 * *  代理对象的方法体的形成就是目标方法+通知
 * *  客户端在context.getBean时，如果该bean有代理对象，则返回代理对象，如果没有代理对象则返回原来的对象
 * 说明：
 * 如果目标类实现了接口，则spring容器会采用jdkproxy,如果目标类没有实现接口，则spring容器会采用
 * cglibproxy
 *
 * @author Administrator
 */
public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    PersonDaoImpl personDao = (PersonDaoImpl) context.getBean("personDao");
    personDao.getPerson();
  }
}
