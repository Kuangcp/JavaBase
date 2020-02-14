package com.github.kuangcp.virusbroadcast.constant;

import java.util.Optional;

/**
 * @author https://github.com/kuangcp on 2020-02-14 12:43
 */
public enum PersonStateEnum {

  NORMAL(PersonState.NORMAL, "normal"),
  SHADOW(PersonState.SHADOW, "shadow"),
  CONFIRMED(PersonState.CONFIRMED, "confirmed"),
  FREEZE(PersonState.FREEZE, "freeze"),
  DEAD(PersonState.DEAD, "dead");

  private int value;
  private String desc;

  public int getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  PersonStateEnum(int value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public static Optional<PersonStateEnum> getByValue(int value) {
    for (PersonStateEnum stateEnum : values()) {
      if (stateEnum.value == value) {
        return Optional.of(stateEnum);
      }
    }
    return Optional.empty();
  }
}
