package com.github.kuangcp.generic.nesting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kuangcp on 19-1-10-下午2:30
 */
class HumanLoader extends AbstractLoader<String, HumanVO> {

  private Map<String, HumanVO> map = new ConcurrentHashMap<>();

  @Override
  Map<String, HumanVO> getMap() {
    return map;
  }
}
