package com.github.kuangcp.virusbroadcast.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Bed extends Point {

  public Bed(int x, int y) {
    super(x, y);
  }

  private boolean isEmpty = true;
}
