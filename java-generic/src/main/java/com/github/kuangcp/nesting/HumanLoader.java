package com.github.kuangcp.nesting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kuangcp on 19-1-10-下午2:30
 */
public class HumanLoader extends AbstractLoader<String, HumanVO> {

  private Map<String, HumanVO> map = new HashMap<>();

  @Override
  Map<String, HumanVO> getMap() {
    return map;
  }

}
