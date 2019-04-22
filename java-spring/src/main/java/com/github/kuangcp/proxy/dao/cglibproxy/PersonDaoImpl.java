package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.proxy.dao.base.Person;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonDaoImpl {

  public void savePerson() {
    log.info("save person");
  }

  public void updatePerson() {
    log.info("update person");
  }

  public void deletePerson() {
    log.info("delete person");
  }

  public List<Person> getPerson() {
    List<Person> personList = new ArrayList<>();
    Person person = new Person();
    person.setId(1L);
    person.setName("who");
    personList.add(person);

    personList.stream().map(Person::toString).forEach(log::info);

    return personList;
  }
}
