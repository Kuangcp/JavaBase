package com.github.kuangcp.jdbc.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PersonDaoImpl3 extends JdbcTemplate implements PersonDao {

  public PersonDaoImpl3(DataSource dataSource) {
    super(dataSource);
  }

  public void savePerson() {
    this.execute(
        "insert into course(cid,cname) values(4,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    return null;
  }
}
