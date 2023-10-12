package com.github.kuangcp.jdbc.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class PersonDaoImpl2 implements PersonDao {

  private JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void savePerson() {
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(4,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    return null;
  }
}
