package com.github.kuangcp.virusbroadcast.domain;

import lombok.Data;

/**
 *
 */
@Data
public class MoveTarget {

  private int x;
  private int y;
  private boolean arrived = false;

  public MoveTarget(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
