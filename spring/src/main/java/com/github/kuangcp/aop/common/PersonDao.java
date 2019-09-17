package com.github.kuangcp.aop.common;

import java.util.List;

/**
 * 目标接口
 */
public interface PersonDao {

  void savePerson();

  void updatePerson();

  void deletePerson();

  List<Person> getPerson();
}
