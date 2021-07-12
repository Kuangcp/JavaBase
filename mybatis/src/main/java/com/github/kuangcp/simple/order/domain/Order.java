package com.github.kuangcp.simple.order.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("normal_order")
public class Order {

    private Long id;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String detail;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer num;
    private LocalDateTime payTime;
}
