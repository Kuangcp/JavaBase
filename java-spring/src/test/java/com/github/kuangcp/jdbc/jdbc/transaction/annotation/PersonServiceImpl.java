package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personService")
public class PersonServiceImpl implements PersonService {

  @Resource(name = "personDao")
  private PersonDao personDao;

  /**
   * 注解的粒度比xml要细
   */
  @Transactional(readOnly = false)
  public void savePerson() {
    // TODO Auto-generated method stub
    this.personDao.savePerson();
  }

}
