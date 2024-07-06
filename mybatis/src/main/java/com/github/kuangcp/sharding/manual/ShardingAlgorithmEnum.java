package com.github.kuangcp.sharding.manual;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-07-11 18:41
 */
@Getter
@AllArgsConstructor
public enum ShardingAlgorithmEnum {
    MOD(ShardingAlgorithmType.MOD, (shardingVal, totalSlice) -> {
        long index = shardingVal % totalSlice;
        return "_" + index;
    }),

    CONSISTENT_HASHING(ShardingAlgorithmType.CONSISTENT_HASHING,
            (shardingVal, totalCount) -> ConsistentHashingAlgorithm.useWithCache(totalCount).get(shardingVal)),
    ;

    private final int type;
    /**
     * shardingVal 用于分片的值
     * totalSlice 总分片数
     * suffix 子表后缀
     */
    private final BiFunction<Long, Integer, String> func;


    public static Optional<ShardingAlgorithmEnum> of(Integer type) {
        if (Objects.isNull(type)) {
            return Optional.empty();
        }
        for (ShardingAlgorithmEnum value : values()) {
            if (Objects.equals(value.type, type)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

}
