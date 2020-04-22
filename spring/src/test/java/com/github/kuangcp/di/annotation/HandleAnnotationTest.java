package com.github.kuangcp.di.annotation;

import com.github.kuangcp.common.SpringHelper;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 原理
 * *  启动spring容器，并且加载配置文件
 * *  会为student和person两个类创建对象
 * *  当解析到<context:annotation-config></context:annotation-config>
 * 会启动依赖注入的注解解析器
 * *  会在纳入spring管理的bean的范围内查找看哪些bean的属性上有@Resource注解
 * *  如果@Resource注解的name属性的值为"",则会把注解所在的属性的名称和spring容器中bean的id进行匹配
 * 如果匹配成功，则把id对应的对象赋值给 该属性，如果匹配不成功，则按照类型进行匹配,如果再匹配不成功，则报错
 * *  如果@Resource注解的name属性的值不为"",会把name属性的值和spring容器中bean的id做匹配，如果匹配
 * 成功，则赋值，如果匹配不成功 ，则直接报错
 * 说明：
 * 注解只能用于引用类型
 *
 * @author Administrator
 */
public class HandleAnnotationTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "di/annotation/applicationContext.xml";
  }

  @Test
  public void test() {
    ClassPathXmlApplicationContext applicationContext = (ClassPathXmlApplicationContext) context;
    Person person = (Person) applicationContext.getBean("person");
    person.say();
    applicationContext.close();
  }
}
