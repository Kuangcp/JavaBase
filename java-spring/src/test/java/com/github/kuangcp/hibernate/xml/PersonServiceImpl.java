package com.github.kuangcp.hibernate.xml;

public class PersonServiceImpl implements PersonService {

  private PersonDao personDao;

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  @Override
  public void savePerson(Person person) {
    // TODO Auto-generated method stub
    this.personDao.savePerson(person);
  }

}
