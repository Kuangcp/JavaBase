package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.base.Person;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonDaoImpl implements PersonDao {

  @Override
  public void savePerson() {
    log.info("save person");
  }

  @Override
  public void updatePerson() {
    log.info("update person");
  }

  @Override
  public void deletePerson() {
    log.info("delete person");
  }

  @Override
  public List<Person> getPerson() {
    Person person = new Person();
    person.setId(1L);
    person.setName("aaa");
    List<Person> personList = new ArrayList<Person>();
    personList.add(person);
    for (Person person2 : personList) {
      log.info("{}", person2);
    }
    return personList;
  }

}
