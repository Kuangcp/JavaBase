package com.github.kuangcp.di.di.xml.set;

import com.github.kuangcp.util.SpringHelper;
import java.util.List;
import org.junit.Test;


public class PersonTest extends SpringHelper {

  @Override
  public String getXmlPath() {
    return "proxy/salary/applicationContext.xml";
  }

  @Test
  public void test() {
    Person person = (Person) context.getBean("person");
    person.getStudent().say();
    System.out.println(person.getPid());
    System.out.println(person.getPname());
    List lists = person.getLists();
    for (int i = 0; i < lists.size(); i++) {
      System.out.println(lists.get(i).toString());
    }
  }
}
