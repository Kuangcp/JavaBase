package com.github.kuangcp.aop.exception.dao;

public class PersonDaoImpl implements PersonDao {

  @Override
  public void savePerson() throws Exception {
    System.out.println("save person");
    int a = 1 / 0;
  }

  @Override
  public void updatePerson() throws Exception {
    System.out.println("update person");
    int a = 1 / 0;
  }

  @Override
  public void deletePerson() throws Exception {
    System.out.println("delete person");
    int a = 1 / 0;
  }
}
