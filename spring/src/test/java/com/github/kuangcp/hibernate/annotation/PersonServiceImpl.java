package com.github.kuangcp.hibernate.annotation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("personService")
public class PersonServiceImpl implements PersonService {

  @Resource(name = "personDao")
  private PersonDao personDao;

  @Transactional(readOnly = false)
  public void savePerson(Person person) {
    this.personDao.savePerson(person);
  }

}
