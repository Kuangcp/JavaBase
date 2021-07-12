package com.github.kuangcp.sharding.manual;

import java.lang.annotation.*;

/**
 * @author https://github.com/kuangcp on 2021-07-11 18:19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ShardingTable {

    int algorithm();

    int tableCount();
}
