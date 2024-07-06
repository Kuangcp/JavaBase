package com.github.kuangcp.proxy.dao.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-09-29 01:30
 */
@Slf4j
public class Permission {

  boolean hasPermission(Object role) {
    log.info("role={}", role);
    return true;
  }
}
