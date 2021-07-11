package com.github.kuangcp.simple.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kuangcp.simple.customer.domain.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDao extends BaseMapper<Customer> {

  @Select({"select * from customer where name like #{name} "})
  List<Customer> queryByName(@Param("name") String name);
}
