package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("personDao")
public class PersonDaoImpl implements PersonDao {

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public void savePerson() {
    this.jdbcTemplate.execute("insert into person(pname) values('aa')");
    int a = 1 / 0;
    this.jdbcTemplate.execute("insert into person(pname) values('aa')");
  }
}
