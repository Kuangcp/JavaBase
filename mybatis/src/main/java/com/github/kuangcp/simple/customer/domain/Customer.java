package com.github.kuangcp.simple.customer.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.kuangcp.sharding.manual.ShardingAlgorithmType;
import com.github.kuangcp.sharding.manual.ShardingTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("customer")
@ShardingTable(algorithm = ShardingAlgorithmType.MOD, tableCount = 6)
public class Customer {

    private Long id;
    private String name;
    private Integer nation;
}
