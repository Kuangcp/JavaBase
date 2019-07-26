package com.github.kuangcp.dao;

import com.github.kuangcp.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

  @Select({"select * from user where name like '#{name}' "})
  User queryByName(@Param("name") String name);
}
