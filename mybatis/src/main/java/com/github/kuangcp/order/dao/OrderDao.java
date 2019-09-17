package com.github.kuangcp.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.kuangcp.order.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao extends BaseMapper<Order> {

}
