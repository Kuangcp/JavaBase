package com.github.kuangcp.virusbroadcast.constant;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-02-04 16:53
 */
public interface PersonState {

  int NORMAL = 0;

  /**
   * 潜伏期
   */
  int SHADOW = NORMAL + 1;

  /**
   * 确诊
   */
  int CONFIRMED = NORMAL + 2;

  /**
   * 隔离
   */
  int FREEZE = NORMAL + 3;

  /**
   * 死亡
   */
  int DEAD = NORMAL + 4;
}
