package com.github.kuangcp.jdbc.jdbc;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao {

  public void savePerson() {
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(4,'aaa')");
    int a = 1 / 0;
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(5,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    return this.getJdbcTemplate().query("select * from course", new PersonRowMapper());
  }
}
