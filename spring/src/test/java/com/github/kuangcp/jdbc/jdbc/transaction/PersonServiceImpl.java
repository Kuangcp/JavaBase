package com.github.kuangcp.jdbc.jdbc.transaction;


public class PersonServiceImpl implements PersonService {

  private PersonDao personDao;

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  @Override
  public void savePerson() {
    this.personDao.savePerson();
  }

}
