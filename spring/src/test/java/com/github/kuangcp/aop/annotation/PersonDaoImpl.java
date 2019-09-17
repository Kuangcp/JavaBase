package com.github.kuangcp.aop.annotation;

import com.github.kuangcp.aop.common.Person;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("personDao")
public class PersonDaoImpl {

  public void savePerson() {
    System.out.println("save person");
  }

  public void updatePerson() {
    System.out.println("update person");
  }

  public void deletePerson() {
    System.out.println("delete person");
  }

  public List<Person> getPerson() {
    Person person = new Person();

    person.setId(1L);
    person.setName("who");
    List<Person> personList = new ArrayList<>();
    personList.add(person);
    for (Person person2 : personList) {
      System.out.println(person2.getName());
    }
    return personList;
  }
}
