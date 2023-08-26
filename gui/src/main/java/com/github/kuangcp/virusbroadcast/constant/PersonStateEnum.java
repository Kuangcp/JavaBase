package com.github.kuangcp.virusbroadcast.constant;

import com.github.kuangcp.virusbroadcast.domain.City;
import lombok.Getter;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * @author https://github.com/kuangcp on 2020-02-14 12:43
 */
@Getter
public enum PersonStateEnum {

    NORMAL(PersonState.NORMAL, "normal", City.Status::setNormal),
    SHADOW(PersonState.SHADOW, "shadow", City.Status::setShadow),
    CONFIRMED(PersonState.CONFIRMED, "confirmed", City.Status::setConfirmed),
    FREEZE(PersonState.FREEZE, "freeze", City.Status::setFreeze),
    DEAD(PersonState.DEAD, "dead", City.Status::setDead);

    private final int value;
    private final String desc;
    private BiConsumer<City.Status, Integer> val;

    PersonStateEnum(int value, String desc, BiConsumer<City.Status, Integer> val) {
        this.value = value;
        this.desc = desc;
        this.val = val;
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
