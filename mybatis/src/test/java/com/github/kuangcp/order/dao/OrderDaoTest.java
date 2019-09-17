package com.github.kuangcp.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kuangcp.base.TestStarter;
import com.github.kuangcp.mock.common.MockUsuallyValue;
import com.github.kuangcp.order.domain.Order;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderDaoTest extends TestStarter {

  @Autowired
  private OrderDao orderDao;

  @Test
  public void testQuery() {
    Order origin = Order.builder()
        .num(1)
        .detail("detail")
        .createTime(LocalDateTime.now())
        .payTime(LocalDateTime.now())
        .build();
    orderDao.insert(origin);
    List<Order> orders = orderDao.selectList(new QueryWrapper<>());
    orders.forEach(item -> log.info("{}", item));
  }

  @Test
  public void testBulkInsert() throws InterruptedException {

    Consumer<Integer> consumer = start -> {
      Order temp = Order.builder().num(MockUsuallyValue.mock(Integer.class))
          .createTime(LocalDateTime.now()).build();
      for (int i = 0; i < 10000; i++) {
        long id = (start + i);
        temp.setId(id);
        temp.setUserId(id % 400);
        orderDao.insert(temp);
      }
    };

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      int finalI = i;
      threads.add(new Thread(() -> consumer.accept(500200 + finalI * 20000)));
    }

    threads.forEach(v -> {
      try {
        v.start();
        v.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread.yield();
  }
}