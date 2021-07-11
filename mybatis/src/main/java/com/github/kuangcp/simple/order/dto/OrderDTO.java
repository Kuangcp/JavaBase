package com.github.kuangcp.simple.order.dto;
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
public class OrderDTO {
  private Long id;
  private Long userId;
  private String username;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private String detail;
  private BigDecimal price;
  private BigDecimal discount;
  private Integer num;
  private LocalDateTime payTime;
}
