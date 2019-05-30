package com.github.kuangcp.aop.exception.action;

import com.github.kuangcp.aop.exception.service.PersonService;

public class PersonAction {

  private PersonService personService;

  public PersonService getPersonService() {
    return personService;
  }

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  public void savePerson() throws Exception {
    this.personService.savePerson();
  }

  public void updatePerson() throws Exception {
    this.personService.updatePerson();
  }

  public void deletePerson() throws Exception {
    this.personService.deletePerson();
  }
}
