package com.github.kuangcp.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kuangcp.order.dao.OrderDao;
import com.github.kuangcp.order.domain.Order;
import com.github.kuangcp.order.dto.OrderDTO;
import com.github.kuangcp.order.service.OrderService;
import com.github.kuangcp.user.dao.UserDao;
import com.github.kuangcp.user.domain.User;
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
  private UserDao userDao;

  @Override
  public List<OrderDTO> queryByUserIdWithLoop(Long userId) {
    List<Order> orders = orderDao.selectList(new QueryWrapper<Order>().eq("user_id", 200));

    return orders.stream().map(this::convert).collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> queryByUserId(Long userId) {
    List<Order> orders = orderDao.selectList(new QueryWrapper<Order>().eq("user_id", 200));

    Set<Long> idSet = orders.stream().map(Order::getUserId).collect(Collectors.toSet());
    Map<Long, User> userMap = userDao.selectBatchIds(idSet).stream()
        .collect(Collectors.toMap(User::getId, Function.identity(), (front, next) -> next));

    return orders.stream().map(v -> convert(v, userMap)).collect(Collectors.toList());
  }

  private OrderDTO convert(Order order, Map<Long, User> userMap) {
    User user = userMap.get(order.getUserId());
    return OrderDTO.builder()
        .count(order.getCount())
        .userId(order.getUserId())
        .username(Optional.ofNullable(user).map(User::getName).orElse(""))
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
    User user = userDao.selectById(order.getUserId());

    return OrderDTO.builder()
        .count(order.getCount())
        .userId(order.getUserId())
        .username(Optional.ofNullable(user).map(User::getName).orElse(""))
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
