package com.github.kuangcp.mvc.annotation;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PersonServiceImpl implements PersonService {

  @Resource(name = "personDao")
  private PersonDao personDao;

  @Override
  public void savePerson() {
    this.personDao.savePerson();
  }
}
