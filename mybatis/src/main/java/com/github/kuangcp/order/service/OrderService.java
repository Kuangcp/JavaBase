package com.github.kuangcp.order.service;

import com.github.kuangcp.order.dto.OrderDTO;
import java.util.List;

public interface OrderService {

  List<OrderDTO> queryByUserIdWithLoop(Long userId);

  List<OrderDTO> queryByUserId(Long userId);
}
