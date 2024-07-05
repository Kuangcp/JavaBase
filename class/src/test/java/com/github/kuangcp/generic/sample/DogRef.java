package com.github.kuangcp.generic.sample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 3/23/19-4:08 PM
 */
@Slf4j
@AllArgsConstructor
public class DogRef implements SampleAble {

  private String refId;
  private int weight;

  @Override
  public int getWeight() {
    return weight;
  }

  @Override
  public String toString() {
    return "DogRef{" +
        "refId='" + refId + '\'' +
        ", weight=" + weight +
        '}';
  }
}
