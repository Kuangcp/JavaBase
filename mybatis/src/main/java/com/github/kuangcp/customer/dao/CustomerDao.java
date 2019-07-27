package com.github.kuangcp.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kuangcp.customer.domain.Customer;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CustomerDao extends BaseMapper<Customer> {

  @Select({"select * from customer where name like #{name} "})
  List<Customer> queryByName(@Param("name") String name);
}
