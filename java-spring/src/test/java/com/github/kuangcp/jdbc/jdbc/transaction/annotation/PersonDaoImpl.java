package com.github.kuangcp.jdbc.jdbc.transaction.annotation;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("personDao")
public class PersonDaoImpl implements PersonDao {

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public void savePerson() {
    // TODO Auto-generated method stub
    this.jdbcTemplate.execute("insert into person(pname) values('aa')");
    int a = 1 / 0;
    this.jdbcTemplate.execute("insert into person(pname) values('aa')");
  }
}
