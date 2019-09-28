package com.github.kuangcp.proxy.dao.cglibproxy;

import com.github.kuangcp.aop.common.Person;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * cglib 实现 无需实现接口
 */
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
    log.info("get person list");
    return Collections.emptyList();
  }
}
