package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("personService")
public class PersonServiceImpl implements PersonService {

  @Resource(name = "personDao")
  private PersonDao personDao;

  /**
   * 注解的粒度比xml要细
   */
  @Transactional(readOnly = false)
  public void savePerson() {
    this.personDao.savePerson();
  }

}
