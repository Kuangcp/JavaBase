package com.github.kuangcp.hi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticJobCommand implements Serializable {

  /**
   * 任务ID
   */
  private String id;


  /**
   * 统计维度
   */
  private Set<ProductStatisticSpan> productStatisticSpan;


  /**
   * 统计起始时间
   */
  private Date startTime;


  /**
   * 统计结束时间
   */
  private Date endTime;

}
