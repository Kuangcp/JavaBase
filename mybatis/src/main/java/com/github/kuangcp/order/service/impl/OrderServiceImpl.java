package com.github.kuangcp.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kuangcp.order.dao.OrderDao;
import com.github.kuangcp.order.domain.Order;
import com.github.kuangcp.order.dto.OrderDTO;
import com.github.kuangcp.order.service.OrderService;
import com.github.kuangcp.customer.dao.CustomerDao;
import com.github.kuangcp.customer.domain.Customer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderDao orderDao;

  @Autowired
  private CustomerDao customerDao;

  @Override
  public List<OrderDTO> queryByUserIdWithLoop(Long userId) {
    List<Order> orders = orderDao.selectList(new QueryWrapper<Order>().eq("user_id", 200));

    return orders.stream().map(this::convert).collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> queryByUserId(Long userId) {
    List<Order> orders = orderDao.selectList(new QueryWrapper<Order>().eq("user_id", 200));

    Set<Long> idSet = orders.stream().map(Order::getUserId).collect(Collectors.toSet());
    Map<Long, Customer> userMap = customerDao.selectBatchIds(idSet).stream()
        .collect(Collectors.toMap(Customer::getId, Function.identity(), (front, next) -> next));

    return orders.stream().map(v -> convert(v, userMap)).collect(Collectors.toList());
  }

  private OrderDTO convert(Order order, Map<Long, Customer> userMap) {
    Customer customer = userMap.get(order.getUserId());
    return OrderDTO.builder()
        .num(order.getNum())
        .userId(order.getUserId())
        .username(Optional.ofNullable(customer).map(Customer::getName).orElse(""))
        .createTime(order.getCreateTime())
        .updateTime(order.getUpdateTime())
        .detail(order.getDetail())
        .price(order.getPrice())
        .discount(order.getDiscount())
        .payTime(order.getPayTime())
        .id(order.getId())
        .build();

  }

  private OrderDTO convert(Order order) {
    Customer customer = customerDao.selectById(order.getUserId());

    return OrderDTO.builder()
        .num(order.getNum())
        .userId(order.getUserId())
        .username(Optional.ofNullable(customer).map(Customer::getName).orElse(""))
        .createTime(order.getCreateTime())
        .updateTime(order.getUpdateTime())
        .detail(order.getDetail())
        .price(order.getPrice())
        .discount(order.getDiscount())
        .payTime(order.getPayTime())
        .id(order.getId())
        .build();
  }
}
