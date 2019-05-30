package com.github.kuangcp.jdbc.jdbc;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class PersonDaoImpl3 extends JdbcTemplate implements PersonDao {

  public PersonDaoImpl3(DataSource dataSource) {
    super(dataSource);
  }

  public void savePerson() {
    // TODO Auto-generated method stub
    this.execute(
        "insert into course(cid,cname) values(4,'aaa')");
  }

  @Override
  public List<Person> getPersons() {
    // TODO Auto-generated method stub
    return null;
  }
}
