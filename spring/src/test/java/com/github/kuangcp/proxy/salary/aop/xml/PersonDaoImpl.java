package com.github.kuangcp.proxy.salary.aop.xml;

import java.util.Collections;
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
    log.info("getPerson");
    return Collections.emptyList();
  }
}
