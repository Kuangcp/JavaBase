package com.github.kuangcp.jdbc.jdbc;

import java.util.List;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao {

  public void savePerson() {
    // TODO Auto-generated method stub
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(4,'aaa')");
    int a = 1 / 0;
    this.getJdbcTemplate().execute(
        "insert into course(cid,cname) values(5,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    // TODO Auto-generated method stub
    return this.getJdbcTemplate().query("select * from course", new PersonRowMapper());
  }
}
