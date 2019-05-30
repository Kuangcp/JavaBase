package com.github.kuangcp.jdbc.transaction;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao {

  @Override
  public void savePerson() {
    // TODO Auto-generated method stub
    this.getJdbcTemplate().execute("insert into person(pname) values('aa')");
    int a = 1 / 0;
    this.getJdbcTemplate().execute("insert into person(pname) values('aa')");
  }

}
