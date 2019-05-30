package com.github.kuangcp.jdbc.jdbc;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class PersonDaoImpl2 implements PersonDao {

  private JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void savePerson() {
    // TODO Auto-generated method stub
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(4,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    // TODO Auto-generated method stub
    return null;
  }
}
