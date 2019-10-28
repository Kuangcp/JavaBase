package com.github.kuangcp.time.wheel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author https://github.com/kuangcp on 2019-10-28 13:10
 */
@Data
@AllArgsConstructor
class TaskNode implements Node {

  private String id;
  private TaskNode next;
  private long lastTime;
  private long delayMills;

  @Override
  public void setNext(Node nodeAble) {
    next = (TaskNode) nodeAble;
  }
}
