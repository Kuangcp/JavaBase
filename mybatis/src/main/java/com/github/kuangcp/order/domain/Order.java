package com.github.kuangcp.order.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_order")
public class Order {

  private Long id;
  private Long userId;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private String detail;
  private BigDecimal price;
  private BigDecimal discount;
  private Integer count;
  private LocalDateTime payTime;

}
