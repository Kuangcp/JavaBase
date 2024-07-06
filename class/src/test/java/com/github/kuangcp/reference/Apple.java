package com.github.kuangcp.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-09-24 12:47
 */
@Data
@Slf4j
@AllArgsConstructor
class Apple {

  private String name;

  /**
   * 覆盖finalize，在回收的时候会执行。
   */
  @Override
  protected void finalize() throws Throwable {
    super.finalize();
//    log.info("Apple: finalize={}", name);
  }
}
