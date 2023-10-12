package com.github.kuangcp.jdbc.jdbc.transaction;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao {

  @Override
  public void savePerson() {
    this.getJdbcTemplate().execute("insert into person(pname) values('aa')");
    int a = 1 / 0;
    this.getJdbcTemplate().execute("insert into person(pname) values('aa')");
  }

}
