package com.github.kuangcp.hibernate.annotation;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personService")
public class PersonServiceImpl implements PersonService {

  @Resource(name = "personDao")
  private PersonDao personDao;

  @Transactional(readOnly = false)
  public void savePerson(Person person) {
    // TODO Auto-generated method stub
    this.personDao.savePerson(person);
  }

}
