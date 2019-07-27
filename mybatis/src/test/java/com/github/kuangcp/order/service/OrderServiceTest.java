package com.github.kuangcp.order.service;

import com.github.kuangcp.base.BaseDaoTest;
import com.github.kuangcp.order.dto.OrderDTO;
import com.github.kuangcp.time.GetRunTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderServiceTest extends BaseDaoTest {

  @Autowired
  private OrderService orderService;

  @Test
  public void testCompareQueryId() {
    GetRunTime run = new GetRunTime().startCount();
    List<OrderDTO> result = orderService.queryByUserIdWithLoop(200L);
    run.endCountOneLine("loop");

    run.startCount();
    result = orderService.queryByUserId(200L);
    run.endCountOneLine("best");
  }
}