package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.aop.common.Person;
import java.util.Collections;
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
    return Collections.emptyList();
  }
}
