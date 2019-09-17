package com.github.kuangcp.aop.exception.service;


import com.github.kuangcp.aop.exception.dao.PersonDao;

public class PersonServiceImpl implements PersonService {

  private PersonDao personDao;

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  @Override
  public void savePerson() throws Exception {
    this.personDao.savePerson();
  }

  @Override
  public void updatePerson() throws Exception {
    this.personDao.updatePerson();
  }

  @Override
  public void deletePerson() throws Exception {
    this.personDao.deletePerson();
  }

}
