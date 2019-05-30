package com.github.kuangcp.nesting;

import java.util.Map;

/**
 * 泛型里面的类自身具有泛型约束, 这样声明才能正确的将类型都约束到
 *
 * @author kuangcp on 19-1-10-下午2:31
 */
abstract class AbstractLoader<R, T extends JsonVO<R>> {

  abstract Map<R, T> getMap();

  void load(String configPath){
    Map<R, T> map = getMap();
    // deserialize data from path by gson or jackson, put data into map
  }
}
