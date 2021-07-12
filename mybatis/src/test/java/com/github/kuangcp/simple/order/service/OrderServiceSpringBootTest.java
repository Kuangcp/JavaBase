package com.github.kuangcp.simple.order.service;

import com.github.kuangcp.base.SpringBootTestStarter;
import com.github.kuangcp.simple.order.dto.OrderDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderServiceSpringBootTest extends SpringBootTestStarter {

  @Autowired
  private OrderService orderService;

  @Test
  public void testCompareQueryId() {
    List<OrderDTO> result = orderService.queryByUserIdWithLoop(200L);
    log.info(": result={}", result);
    result = orderService.queryByUserId(200L);
    log.info(": result={}", result);
  }
}