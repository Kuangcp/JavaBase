package com.github.kuangcp.proxy.salary.aop.xml;

import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl {


  public void savePerson() {
    // TODO Auto-generated method stub
    System.out.println("save person");
  }


  public void updatePerson() {
    // TODO Auto-generated method stub
    System.out.println("update person");
  }


  public void deletePerson() {
    // TODO Auto-generated method stub
    System.out.println("delete person");
  }


  public List<Person> getPerson() {
    // TODO Auto-generated method stub
    Person person = new Person();

    person.setPid(1L);
    person.setPname("aaa");
    List<Person> personList = new ArrayList<Person>();
    personList.add(person);
    for (Person person2 : personList) {
      System.out.println(person2.getPname());
    }
    return personList;
  }

}
