package com.github.kuangcp.wildcard;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kuangcp on 3/21/19-5:44 PM
 */
@Data
@AllArgsConstructor
public class Unit<K> implements SampleAble<K> {

  private int index;
  private K id;
  private int weight;

  @Override
  public K getId() {
    return id;
  }

  @Override
  public int getWeight() {
    return weight;
  }
}
