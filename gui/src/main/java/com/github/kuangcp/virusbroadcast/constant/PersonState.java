package com.github.kuangcp.virusbroadcast.constant;

/**
 * @author https://github.com/kuangcp on 2020-02-04 16:53
 */
public interface PersonState {

  int NORMAL = 0;

  // 疑似
  int SUSPECTED = NORMAL + 1;

  // 潜伏期
  int SHADOW = NORMAL + 2;

  int CONFIRMED = NORMAL + 3;

  // 隔离
  int FREEZE = NORMAL + 4;

  // 治愈
  int CURED = NORMAL + 5;
}
