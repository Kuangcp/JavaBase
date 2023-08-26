package com.github.kuangcp.simple.order.service;

import com.github.kuangcp.simple.order.dto.OrderDTO;
import java.util.List;

public interface OrderService {

  List<OrderDTO> queryByUserIdWithLoop(Long userId);

  List<OrderDTO> queryByUserId(Long userId);
}
