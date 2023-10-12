package com.github.kuangcp.jdbc.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper {

  @Override
  public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    Person person = new Person();
    person.setPid(rs.getLong("cid"));
    person.setPname(rs.getString("cname"));
    return person;
  }

  /**
   * 练习：
   *    用spring集合jdbc完成crud的操作
   *      查询：
   *         查询一张表中所有的数据
   *         根据主键查询某一行数据
   *         根据条件查询数据
   */

}
