package com.github.kuangcp.jdbc.jdbc;

import java.util.List;

public interface PersonDao {

  void savePerson();

  List<Person> getPersons();
}
