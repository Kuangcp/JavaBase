package com.github.kuangcp.sharding.manual;

import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-07-11 18:19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ShardingTable {

    int algorithm();

    int tableCount();
}
