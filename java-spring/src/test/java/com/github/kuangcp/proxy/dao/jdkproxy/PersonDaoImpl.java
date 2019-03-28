package com.github.kuangcp.proxy.dao.jdkproxy;

import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {

  @Override
  public void savePerson() {
    // TODO Auto-generated method stub
    System.out.println("save person");
  }

  @Override
  public void updatePerson() {
    // TODO Auto-generated method stub
    System.out.println("update person");
  }

  @Override
  public void deletePerson() {
    // TODO Auto-generated method stub
    System.out.println("delete person");
  }

  @Override
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
