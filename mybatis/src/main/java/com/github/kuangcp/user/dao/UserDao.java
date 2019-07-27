package com.github.kuangcp.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kuangcp.user.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDao extends BaseMapper<User> {

  @Select({"select * from user where name like #{name} "})
  List<User> queryByName(@Param("name") String name);
}
